/*
 * Copyright 2014 adavis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*
 * Copyright 2014 adavis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package explore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;
import static org.bitbucket.dollar.lang.Lists.newList;
import static org.bitbucket.dollar.lang.Tuple.t;
import org.bitbucket.dollar.lang.Tuples.Tuple3;

/**
 *
 * @author adavis
 */
public class Commands {

    // name of command, number, command
    public static final List<Tuple3<String, Integer, Runnable>> commands
            = newList(t("create-project", 1, Explore::handleCreateProject),
                    t("fibonacci", 2, Explore::handleFibonacci));

    public static final Map<String, Runnable> strMap = new HashMap<>();

    public static final Map<Integer, Runnable> intMap = new HashMap<>();

    static {
        // fill strMap and intMap assuming each key appears only once:
        commands.stream().collect(groupingBy(Tuple3::get_1))
                .forEach((key, tlist) -> strMap.put(key, tlist.get(0).get_3()));

        commands.stream().collect(groupingBy(Tuple3::get_2))
                .forEach((key, tlist) -> intMap.put(key, tlist.get(0).get_3()));
    }

}
