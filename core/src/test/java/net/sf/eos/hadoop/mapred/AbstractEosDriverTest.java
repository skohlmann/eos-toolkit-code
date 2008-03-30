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
package net.sf.eos.hadoop.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.ToolRunner;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class AbstractEosDriverTest extends AbstractEosDriver {

    @Test
    public void longDestination() throws Exception {
        final String param = "destination";
        run(new String[] {"--" + DESTINATION_LONG_CMD_ARG, param});
        final JobConf conf = getJobConf();
        final Path p = conf.getOutputPath();
        assertEquals(param, p.getName());
    }

    @Test
    public void shortDestination() throws Exception {
        final String param = "dest";
        run(new String[] {"-" + DESTINATION_SHORT_CMD_ARG, param});
        final JobConf conf = getJobConf();
        final Path p = conf.getOutputPath();
        assertEquals(param, p.getName());
    }

    @Test
    public void longSource() throws Exception {
        final String param = "tester";
        run(new String[] {"--" + SOURCE_LONG_CMD_ARG, param});
        final JobConf conf = getJobConf();
        final Path[] p = conf.getInputPaths();
        assertEquals(param, p[0].getName());
    }

    @Test
    public void shortSource() throws Exception {
        final String param = "test";
        run(new String[] {"-" + SOURCE_SHORT_CMD_ARG, param});
        final JobConf conf = getJobConf();
        final Path[] p = conf.getInputPaths();
        assertEquals(param, p[0].getName());
    }

    @Test
    public void toolRunner() throws Exception {
        final String args[] = new String[] {"-D", "abc=123", "-D", "xyz=789"};
        final int res = ToolRunner.run(new Configuration(), this, args);
        final JobConf conf = getJobConf();
        assertEquals("123", conf.get("abc"));
        assertEquals("789", conf.get("xyz"));
    }

    @Test(expected=IllegalStateException.class)
    public void illegalState() {
        getJobConf();
    }

    public AbstractEosDriverTest() {
        return;
    }
}
