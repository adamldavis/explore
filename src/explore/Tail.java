/*
 * Copyright 2014 Adam L. Davis.
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

import java.util.stream.Stream;

/**
 * Implements tail-call using Java 8 Stream.
 * 
 * @author adavis
 */
public interface Tail<T> {
    
    Tail<T> apply();
    
    default boolean isDone() {
        return false;
    }
    
    default T result() {
        throw new UnsupportedOperationException("Not done yet.");
    }
    
    default T invoke() {
        return Stream.iterate(this, Tail::apply)
                .filter(Tail::isDone)
                .findFirst()
                .get()
                .result();
    }
    
    static <T> Tail<T> done(final T value) {
        return new Tail<T>() {
            @Override
            public T result() {
                return value;
            }  
            @Override
            public boolean isDone() {
                return true;
            }
            @Override
            public Tail<T> apply() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
}
