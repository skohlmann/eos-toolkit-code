/* Copyright (c) 2008 Sascha Kohlmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Logger;

import net.sf.eos.Experimental;
import net.sf.eos.Function;
import net.sf.eos.Nullable;
import net.sf.eos.Predicate;
import net.sf.eos.util.Compositions;
import net.sf.eos.util.Predicates;

import static net.sf.eos.util.Predicates.not;
import static net.sf.eos.util.Conditions.checkNotNull;

/**
 * @author Sascha Kohlmann
 * @since 0.2.0
 * @param <T> the type of the support
 */
@Experimental
public abstract class InjectionSupport<T> {

    /** The logger for the given class. */
    static final Logger LOG = Logger.getLogger(InjectionSupport.class.getName());

    @SuppressWarnings("unchecked")
    private static final InjectionSupport INSTANCE = new DefaultInjectionSupport();

    /**
     * Returns an instance of injector.
     * @param <T> the generic type of the class to inject
     * @return an injector. Never {@code null}
     */
    @SuppressWarnings("unchecked")
    public static final <T> InjectionSupport<T> getInstance() {
        return INSTANCE;
    }

    /**
     * Injects all by {@link Inject} declared resources in the given instance {@code T}.
     * @param t the resource to inject declared resources
     * @throws InjectionException if the process fails
     */
    public abstract void inject(final T t);

    /**
     * The default implementation of the support.
     * @author Sascha Kohlmann
     * @param <T> the type
     */
    static final class DefaultInjectionSupport<T> extends InjectionSupport<T> {

        private static final String BASE = "META-INF/services/";

        private final Function<T, Collection<Method>> filter;
        private final InjectAnnotationFetcher fetcher = new InjectAnnotationFetcher();
        private final OneParameterTypePredicate oneParameter = new OneParameterTypePredicate();
        private final Predicate<Class<?>> injectRuleChain = new InjectParameterRulePredicate();

        public DefaultInjectionSupport() {
            // Create filter chain
            LOG.fine("start function chain creation");
            final Function<Collection<Method>, Collection<Method>> voidFilter =
                Compositions.compose(new SetPrefixFilter(), new VoidReturnTypeFilter());

            final Function<Collection<Method>, Collection<Method>> oneParameterType =
                Compositions.compose(voidFilter, new OneParameterTypeFilter());

            this.filter = Compositions.compose(new IndentifyInjectAnnotatedMethods<T>(),
                                               oneParameterType);
            LOG.fine("finish function chain creation");
        }

        @Override
        public void inject(final T t) {
            assert this.filter != null;
            final Collection<Method> methods = this.filter.apply(t);
            for (final Method m : methods) {
                final Inject annotation = this.fetcher.apply(m);
                if (annotation != null) {
                    final String name = annotation.className();
                    final String className = serviceClassNameLookup(BASE, name);
                    try {
                        final Class<?> clazz = createClass(className, m);
                        final Object instance = createInstance(clazz);
                        m.invoke(t, instance);
                    } catch (final Exception e) {
                        if (e instanceof InjectionException) {
                            throw (InjectionException) e;
                        }
                        throw new InjectionException("unable to inject instance '" + className
                                                     + "' in method '" + m + " of class '"
                                                     + t.getClass().getName() + "'",
                                                     e);
                    }
                }
            }
        }

        /**
         * Looks up for the name of the implementation in the <em>base</em> directory. If the
         * file does not exists, the given file name will return.
         * @param base the base directory
         * @param fileName the name of the file in the base directory containing the name of
         *                 the concrete class.
         * @return the name of the concrete class.
         * @throws InjectionException if there is an error in the configuration line system
         */
        String serviceClassNameLookup(final String base, final String fileName) {
            String fullName = base + fileName;
            final ClassLoader cl = getContextClassLoader();
            final InputStream in = cl.getResourceAsStream(fullName);
            if (in == null) {
                return fileName;
            }
            try {
                final Reader reader = new InputStreamReader(in, "utf-8");
                final BufferedReader br = new BufferedReader(reader);
                String line = null;
                while ((line = br.readLine()) != null) {
                    final String className = parseLine(line);
                    if (className != null) {
                        return className;
                    }
                }
                throw new InjectionException("No value in service file for " + fullName);
            } catch (final IOException e) {
                throw new InjectionException("Unable to get service file for " + fullName, e);
            } finally {
                try {
                    in.close();
                } catch (final IOException e) {
                    throw new InjectionException(e);
                }
            }
        }

        /**
         * Parses a line in a service file.
         * @param line the line to parse
         * @return the name of the class of {@code null} if the line contains no class name
         * @throws IOException if an IO error comes up
         * @throws InjectionException if there is an error in the configuration line
         */
        String parseLine(final String line) throws IOException {
            assert line != null;
            final String namePart;
            final int ci = line.indexOf('#');
            if (ci >= 0) {
                namePart = line.substring(0, ci);
            } else {
                namePart = line;
            }
            final String adjustedName = namePart.trim();
            final int n = adjustedName.length();
            if (n != 0) {
                if (adjustedName.indexOf(' ') >= 0 || adjustedName.indexOf('\t') >= 0) {
                    throw new InjectionException("Illegal configuration-file syntax");
                }
                int cp = adjustedName.codePointAt(0);
                if (!Character.isJavaIdentifierStart(cp)) {
                    throw new InjectionException("Illegal provider-class name");
                }
                for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                    cp = adjustedName.codePointAt(i);
                    if (!Character.isJavaIdentifierPart(cp) && cp != '.') {
                        throw new InjectionException("Illegal provider-class name");
                    }
                }
                return adjustedName;
            }
            return null;
        }

        /**
         * Creates an instance of the given class. The class must follow the rules of
         * {@link Inject}.
         * @param clazz the class of the instance to create
         * @return an instance of class if it is possible to create
         * @throws InjectionException if it is unable to create the instance
         */
        @SuppressWarnings("unchecked")
        Object createInstance(final Class<?> clazz) {
            if (! this.injectRuleChain.evaluate(clazz)) {
                // TODO better exception description. Maybe i18n support
                throw new InjectionException("Unable to create instance for '" + clazz.getName()
                                             + "'. Implementation doesn't follow the rules of "
                                             + " '" + Inject.class.getName() + "'.");
            }

            try {
                return (T) clazz.newInstance();
            } catch (final Exception e) {
                throw new InjectionException("unable to create instance for '" + clazz.getName()
                                             + "'",
                                             e);
            }
        }

        /**
         * Creates a class. First tries to create the class from the given <em>className</em> If
         * fail tries to get the class of the only parameter of the given <em>method</em>. Throws
         * an exception if the inject rule fails (one parameter only for the method).
         * @param className the name of the class to create
         * @param method the method to get the first parameter type from if creation from
         *               <em>className</em> fails.
         * @return a class
         * @throws InjectionException if it is unable to create a class instance.
         */
        Class<?> createClass(final String className, final Method method) {
            assert className != null;
            assert method != null;
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (final ClassNotFoundException e) {
                LOG.fine("Unable to create '" + className + "' class. "
                         + "Try to get parameter type instance.");
                if (! this.oneParameter.evaluate(method)) {
                    final Class<?>[] parameter = method.getParameterTypes();
                    throw new InjectionException("Method '" + method.getName() + "' of class '"
                                                 + method.getDeclaringClass().getName()
                                                 + "' contains fewer or more than one parameter: "
                                                 + parameter.length);
                }
                clazz = method.getParameterTypes()[0];
            }
            return clazz;
        }

        /**
         * Returns the thread context class loader if available.
         * <p>The thread context class loader is available for JDK 1.2 or later
         * if certain security conditions are met.</p>
         *
         * @return the context classloader.
         */
        static ClassLoader getContextClassLoader() {

            final ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();

            LOG.finer("context class loader of " + Thread.currentThread() + ", "
                    + Thread.currentThread().getName() + ": " + classLoader);
            return classLoader;
        }

        /** Checks whether the given {@link Class} represents an
         * {@link java.lang.annotation.Annotation}.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsAnnotation implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} represents an
             * {@link java.lang.annotation.Annotation}.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents an
             *         {@code Annotation}. {@code false} otherwise. */
            public boolean evaluate(Class<?> clazz) {
                return clazz.isAnnotation();
            }
        }

//        /** Checks whether the given {@link Class} represents a local class.
//         * @author Sascha Kohlmann */
//        @Singleton
//        public static class IsLocalClass implements Predicate<Class<?>> {
//            /** Checks whether the given {@link Class} represents a local class.
//             * @param clazz the {@code Class} instance to check.
//             * @return {@code true} if and only if the {@code Class} represents a local class.
//             *         {@code false} otherwise. */
//            public boolean evaluate(Class<?> clazz) {
//                return clazz.isLocalClass();
//            }
//        }

        /** Checks whether the given {@link Class} represents a member class.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsMemberClass implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} represents a member class.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents a member class.
             *         {@code false} otherwise. */
            public boolean evaluate(Class<?> clazz) {
                return clazz.isMemberClass();
            }
        }


        /** Checks whether the given {@link Class} represents an {@link Enum}.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsEnum implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} represents an {@link Enum}.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents an
             *         {@code Enum}. {@code false} otherwise. */
            public boolean evaluate(Class<?> clazz) {
                return clazz.isEnum();
            }
        }

        /** Checks whether the given {@link Class} represents an {@link Interface}.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsInterface implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} represents an {@link Interface}.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents an
             *         {@code Interface}. {@code false} otherwise. */
            public boolean evaluate(Class<?> clazz) {
                return clazz.isInterface();
            }
        }

        /** Checks whether the given {@link Class} represents an array class.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsArray implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} represents an array class.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents an
             *         array class. {@code false} otherwise. */
            public boolean evaluate(Class<?> clazz) {
                return clazz.isArray();
            }
        }

        /** Checks whether the given {@link Class} represents a primitive type.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsPrimitive implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} represents a primitive type.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} a primitive type.
             *         {@code false} otherwise. */
            public boolean evaluate(Class<?> clazz) {
                return clazz.isPrimitive();
            }
        }

        /** Checks whether the given {@link Class} is an <em>abstract</em> class.
         * @author Sascha Kohlmann
         * @see java.lang.reflect.Modifier */
        @Singleton
        public static class IsAbstract implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} is an <em>abstract</em> class.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents an
             *         <em>abstract</em> class. {@code false} otherwise.
             * @see java.lang.reflect.Modifier */
            public boolean evaluate(Class<?> clazz) {
                final int mod = clazz.getModifiers();
                return Modifier.isAbstract(mod);
            }
        }

        /** Checks whether the given {@link Class} is an <em>static</em> class.
         * @author Sascha Kohlmann
         * @see java.lang.reflect.IsNotStatic
         * @see java.lang.reflect.Modifier */
        @Singleton
        public static class IsStatic implements Predicate<Class<?>> {
            /** Checks whether the given {@link Class} is an <em>static</em> class.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents an
             *         <em>static</em> class. {@code false} otherwise.
             * @see java.lang.reflect.IsNotStatic
             * @see java.lang.reflect.Modifier */
            public boolean evaluate(Class<?> clazz) {
                final int mod = clazz.getModifiers();
                return Modifier.isStatic(mod);
            }
        }

        /** Checks whether the given {@link Class} is not an <em>static</em> class.
         * @author Sascha Kohlmann
         * @see java.lang.reflect.IsStatic
         * @see java.lang.reflect.Modifier */
        @Singleton
        public static class IsNotStatic implements Predicate<Class<?>> {
            private static final IsStatic STATIC_TEST = new IsStatic();
            /** Checks whether the given {@link Class} is not an <em>static</em> class.
             * @param clazz the {@code Class} instance to check.
             * @return {@code true} if and only if the {@code Class} represents not an
             *         <em>static</em> class. {@code false} otherwise.
             * @see java.lang.reflect.IsStatic
             * @see java.lang.reflect.Modifier */
            public boolean evaluate(Class<?> clazz) {
                return ! STATIC_TEST.evaluate(clazz);
            }
        }

        /** Checks the rules from {@link Inject}.
         * @author Sascha Kohlmann */
        @Singleton
        public static class IsPlainOldJavaClassPredicate implements Predicate<Class<?>> {
            @SuppressWarnings("unchecked")
            private static final Predicate<Class<?>> TYPE_CHAIN = Predicates.or(new IsAnnotation(),
                                                                                new IsInterface(),
                                                                                new IsEnum(),
                                                                                new IsArray(),
                                                                                new IsPrimitive());
            /** @return {@code true} if and only if the given <em>clazz</em> is not an
             *          annotation type, not an interface type, not an enumeration type, not a
             *          array type and not a primitive type. {@code false} otherwise.
             * @param clazz the Class instance to test
             */
            public boolean evaluate(final Class<?> clazz) {
                return ! TYPE_CHAIN.evaluate(clazz);
            }
        }

        /** Checks the rules from {@link Inject}.
         * @author Sascha Kohlmann */
        @Singleton
        public static class InjectParameterRulePredicate implements Predicate<Class<?>> {
            @SuppressWarnings("unchecked")
            private static final Predicate<Class<?>> TYPE_CHAIN =
                    Predicates.and(new IsPlainOldJavaClassPredicate(), not(new IsAbstract()));
            private static final Predicate<Class<?>> IS_MEMBER_CLASS = new IsMemberClass();
            private static final Predicate<Class<?>> IS_NOT_STATIC = new IsNotStatic();
            /** @return {@code true} if and only if the given <em>clazz</em> is not an
             *          annotation type, not an interface type, not an enumeration type, not a
             *          array type and not a primitive type. {@code false} otherwise.
             * @param clazz the Class instance to test
             */
            public boolean evaluate(final Class<?> clazz) {
                if (IS_MEMBER_CLASS.equals(clazz) && IS_NOT_STATIC.evaluate(clazz)) {
                    return false;
                }
                return TYPE_CHAIN.evaluate(clazz);
            }
        }

        /**
         * Returns the filter instance.
         * @return never {@code null}
         */
        Function<T, Collection<Method>> getFilter() {
            assert this.fetcher != null;
            return this.filter;
        }

        /**
         * Filters out all methods whose name prefix doesn't start with "{@code set}".
         * @author Sascha Kohlmann
         * @since 1.0
         */
        @Singleton
        public static class SetPrefixFilter
                implements Function<Collection<Method>, Collection<Method>> {

            /**
             * Filters out all methods whose name prefix doesn't start with "{@code set}".
             * @param methods a collection of methods to filter
             * @return Contains only methods whose name prefixed with "{@code set}".
             *         Never {@code null}
             */
            public Collection<Method> apply(@Nullable final Collection<Method> methods) {
                if (methods == null) {
                    return Collections.emptySet();
                }
                final Collection<Method> retval = new HashSet<Method>();
                for (final Method m : methods) {
                    final String name = m.getName();
                    if (name.startsWith("set")) {
                        retval.add(m);
                    }
                }
                return retval;
            }
        }

        /**
         * Filters out all methods where the return type is not {@link Void} or {@code void}.
         * @author Sascha Kohlmann
         * @since 1.0
         */
        @Singleton
        public static class VoidReturnTypeFilter
                implements Function<Collection<Method>, Collection<Method>> {

            /**
             * Filters out all methods where the return type is not {@link Void} or {@code void}.
             * @param methods a collection of methods to filter
             * @return Contains only methods whose return type is {@link Void}. Never {@code null}
             */
            public Collection<Method> apply(@Nullable final Collection<Method> methods) {
                if (methods == null) {
                    return Collections.emptySet();
                }
                final Collection<Method> retval = new HashSet<Method>();
                for (final Method m : methods) {
                    final Class<?> returnType = m.getReturnType();
                    if (returnType == Void.TYPE || returnType == Void.class) {
                        retval.add(m);
                    }
                }
                return retval;
            }
        }

        /**
         * Filters out all methods with one and only one parameter type.
         * @author Sascha Kohlmann
         * @since 1.0
         */
        @Singleton
        public static class OneParameterTypeFilter
                implements Function<Collection<Method>, Collection<Method>> {

            private final OneParameterTypePredicate predicate = new OneParameterTypePredicate();

            /**
             * Filters out all methods with one and only one parameter type.
             * @param methods a collection of methods to filter
             * @return Contains only methods with one and only one parameter type.
             *         Never {@code null}
             */
            public Collection<Method> apply(@Nullable final Collection<Method> methods) {
                if (methods == null) {
                    return Collections.emptySet();
                }
                final Collection<Method> retval = new HashSet<Method>();
                for (final Method m : methods) {
                    if (this.predicate.evaluate(m)) {
                        retval.add(m);
                    }
                }
                return retval;
            }
        }

        /**
         * Test if the given method has only one parameter type.
         * @author Sascha Kohlmann
         */
        @Singleton
        public static class OneParameterTypePredicate implements Predicate<Method> {
            /**
             * Test if the given method has only one parameter type.
             * @param method the method to test
             * @return {@code true} if and only if <em>method</em> has one parameter type.
             *         {@code false} otherwise.
             */
            public boolean evaluate(final Method method) {
                checkNotNull(method);
                return method.getParameterTypes().length == 1 ? true : false;
            }
        }

        /**
         * Identifies all methods of the given instance {@code T} which are annotated with
         * {@link Inject}.
         * @author Sascha Kohlmann
         * @param <T> the type of the instance to introspect
         */
        @Singleton
        public static class IndentifyInjectAnnotatedMethods<T>
                implements Function<T, Collection<Method>> {

            private final Predicate<Method> injectPredicate;

            public IndentifyInjectAnnotatedMethods() {
                this.injectPredicate = Compositions.compose(new InjectAnnotationFetcher(),
                                                            new NotNullPredicate<Inject>());
            }

            /**
             * Identifies all methods of the given instance {@code T} which are annotated with
             * {@link Inject}.
             * @param methods a collection of methods to filter
             * @return Contains only methods annotated with {@link Inject}. Never {@code null}
             */
            public Collection<Method> apply(@Nullable final T methods) {

                if (methods == null) {
                    return Collections.emptySet();
                }

                assert this.injectPredicate != null;
                final Class<?> clazz = methods.getClass();
                final Method[] methodList = clazz.getMethods();
                final Collection<Method> retval = new HashSet<Method>();
                for (final Method m : methodList) {
                    if (this.injectPredicate.evaluate(m)) {
                        retval.add(m);
                    }
                }
                return retval;
            }
        }

        /**
         * Fetches a given {@link Inject} annotation from the method if available.
         * @author Sascha Kohlmann
         */
        @Singleton
        public static class InjectAnnotationFetcher implements Function<Method, Inject> {

            /**
             * Fetches a given {@link Inject} annotation from the method if available.
             * @param method the method to fetch the {@link Inject} annotation from
             * @return the {@link Inject} annotation if available. Otherwise {@code null}
             */
            public Inject apply(@Nullable final Method method) {
                if (method == null) {
                    return null;
                }
                return method.getAnnotation(Inject.class);
            }
        }

        /** Tests if the given argument is not {@code null}.
         * @author Sascha Kohlmann
         */
        @Singleton
        public static class NotNullPredicate<T> implements Predicate<T> {
            /** Tests if the given argument is not {@code null}.
             * @return {@code true} if the value is not {@code null}. {@code false} otherwise */
            public boolean evaluate(@Nullable final T value) {
                return value != null ? true : false;
            }
        }
    }
}

