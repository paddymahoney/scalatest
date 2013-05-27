/*
 * Copyright 2001-2013 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest

import org.scalautils.Equality
import org.scalautils.Normalization
import org.scalautils.StringNormalizations._
import SharedHelpers._
import FailureMessages.prettifyArrays

class ListShouldContainAtLeastOneOfSpec extends Spec with Matchers {

  val invertedStringEquality =
    new Equality[String] {
      def areEqual(a: String, b: Any): Boolean = a != b
    }
  
  //ADDITIONAL//

  object `a List` {

    val fumList: List[String] = List("fum")
    val toList: List[String] = List("to")

    object `when used with contain atLeastOneOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should newContain atLeastOneOf ("fee", "fie", "foe", "fum")
        val e1 = intercept[TestFailedException] {
          fumList should newContain atLeastOneOf ("happy", "birthday", "to", "you")
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (Resources("didNotContainAtLeastOneOf", prettifyArrays(fumList), "\"happy\", \"birthday\", \"to\", \"you\""))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        fumList should newContain atLeastOneOf ("happy", "birthday", "to", "you")
        intercept[TestFailedException] {
          fumList should newContain atLeastOneOf ("fum", "fum", "fum", "fum")
        }
      }
      def `should use an explicitly provided Equality` {
        (fumList should newContain atLeastOneOf ("happy", "birthday", "to", "you")) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (fumList should newContain atLeastOneOf ("fum", "fum", "fum", "fum")) (decided by invertedStringEquality)
        }
        intercept[TestFailedException] {
          fumList should newContain atLeastOneOf (" FEE ", " FIE ", " FOE ", " FUM ")
        }
        (fumList should newContain atLeastOneOf (" FEE ", " FIE ", " FOE ", " FUM ")) (after being lowerCased and trimmed)
      }
    }

    object `when used with (contain atLeastOneOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {

        fumList should (newContain atLeastOneOf ("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain atLeastOneOf ("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (Resources("didNotContainAtLeastOneOf", prettifyArrays(fumList), "\"happy\", \"birthday\", \"to\", \"you\""))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        fumList should (newContain atLeastOneOf ("happy", "birthday", "to", "you"))
        intercept[TestFailedException] {
          fumList should (newContain atLeastOneOf ("fum", "fum", "fum", "fum"))
        }
      }
      def `should use an explicitly provided Equality` {
        (fumList should (newContain atLeastOneOf ("happy", "birthday", "to", "you"))) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (fumList should (newContain atLeastOneOf ("fum", "fum", "fum", "fum"))) (decided by invertedStringEquality)
        }
        intercept[TestFailedException] {
          fumList should (newContain atLeastOneOf (" FEE ", " FIE ", " FOE ", " FUM "))
        }
        (fumList should (newContain atLeastOneOf (" FEE ", " FIE ", " FOE ", " FUM "))) (after being lowerCased and trimmed)
      }
    }

/*
 I purposely don't want to support this syntax:

        fumList should newContain (atLeastOneOf ("fee", "fie", "foe", "fum"))
        fumList should (newContain (atLeastOneOf ("fee", "fie", "foe", "fum")))

 Reason is that I don't want people putting parentheses between contain and atLeastOneOf, etc. This will not compile.
*/
    object `when used with not contain atLeastOneOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        toList should not newContain atLeastOneOf ("fee", "fie", "foe", "fum")
        val e1 = intercept[TestFailedException] {
          toList should not newContain atLeastOneOf ("happy", "birthday", "to", "you")
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (Resources("containedAtLeastOneOf", prettifyArrays(toList), "\"happy\", \"birthday\", \"to\", \"you\""))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        toList should not newContain atLeastOneOf ("to", "to", "to", "to")
        intercept[TestFailedException] {
          toList should not newContain atLeastOneOf ("fee", "fie", "foe", "fum")
        }
      }
      def `should use an explicitly provided Equality` {
        (toList should not newContain atLeastOneOf ("to", "to", "to", "to")) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (toList should not newContain atLeastOneOf ("fee", "fie", "foe", "fum")) (decided by invertedStringEquality)
        }
        toList should not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO ")
        intercept[TestFailedException] {
          (toList should not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO ")) (after being lowerCased and trimmed)
        }
      }
    }

/*
Interesting, of these three, the top one does happen to compile and run:

        toList should not newContain (atLeastOneOf ("fee", "fie", "foe", "fum"))
        // toList should not (newContain (atLeastOneOf ("fee", "fie", "foe", "fum")))
        // toList should (not (newContain (atLeastOneOf ("fee", "fie", "foe", "fum"))))

The bottom two don't, but still I don't want to support that in general.
*/
    object `when used with (not contain atLeastOneOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        toList should (not newContain atLeastOneOf ("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          toList should (not newContain atLeastOneOf ("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message.get should be (Resources("containedAtLeastOneOf", prettifyArrays(toList), "\"happy\", \"birthday\", \"to\", \"you\""))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        toList should (not newContain atLeastOneOf ("to", "to", "to", "to"))
        intercept[TestFailedException] {
          toList should (not newContain atLeastOneOf ("fee", "fie", "foe", "fum"))
        }
      }
      def `should use an explicitly provided Equality` {
        (toList should (not newContain atLeastOneOf ("to", "to", "to", "to"))) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (toList should (not newContain atLeastOneOf ("fee", "fie", "foe", "fum"))) (decided by invertedStringEquality)
        }
        toList should (not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO "))
        intercept[TestFailedException] {
          (toList should (not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO "))) (after being lowerCased and trimmed)
        }
      }
    }
  }

  object `a collection of Lists` {

    val list1s: Vector[List[Int]] = Vector(List(1), List(1), List(1))
    val lists: Vector[List[Int]] = Vector(List(1), List(1), List(2))
    val nils: Vector[List[Int]] = Vector(Nil, Nil, Nil)
    val listsNil: Vector[List[Int]] = Vector(List(1), List(1), Nil)
    val hiLists: Vector[List[String]] = Vector(List("hi"), List("hi"), List("hi"))
    val toLists: Vector[List[String]] = Vector(List("to"), List("to"), List("to"))

    object `when used with contain atLeastOneOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should newContain atLeastOneOf (1, 3, 4)
        atLeast (2, lists) should newContain atLeastOneOf (1, 3, 4)
        atMost (2, lists) should newContain atLeastOneOf (2, 3, 4)
        no (lists) should newContain atLeastOneOf (3, 4, 5)
        no (nils) should newContain atLeastOneOf (1, 3, 4)
        no (listsNil) should newContain atLeastOneOf (3, 4, 5)

        val e1 = intercept[TestFailedException] {
          all (lists) should newContain atLeastOneOf (1, 3, 4)
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 2, " + prettifyArrays(List(2)) + " did not contain at least one of (1, 3, 4) (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(lists)))

        val e2 = intercept[TestFailedException] {
          all (nils) should newContain atLeastOneOf ("ho", "hey", "howdy")
        }
        e2.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e2.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e2.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 0, " + prettifyArrays(Nil) + " did not contain at least one of (\"ho\", \"hey\", \"howdy\") (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(nils)))

        val e4 = intercept[TestFailedException] {
          all (listsNil) should newContain atLeastOneOf (1, 3, 4)
        }
        e4.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e4.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e4.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 2, " + prettifyArrays(Nil) + " did not contain at least one of (1, 3, 4) (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(listsNil)))
      }

      def `should use the implicit Equality in scope` {
        all (hiLists) should newContain atLeastOneOf ("hi")
        intercept[TestFailedException] {
          all (hiLists) should newContain atLeastOneOf ("ho")
        }
        implicit val ise = invertedStringEquality
        all (hiLists) should newContain atLeastOneOf ("ho")
        intercept[TestFailedException] {
          all (hiLists) should newContain atLeastOneOf ("hi")
        }
      }
      def `should use an explicitly provided Equality` {
        (all (hiLists) should newContain atLeastOneOf ("ho")) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (all (hiLists) should newContain atLeastOneOf ("hi")) (decided by invertedStringEquality)
        }
        implicit val ise = invertedStringEquality
        (all (hiLists) should newContain atLeastOneOf ("hi")) (decided by defaultEquality[String])
        intercept[TestFailedException] {
          (all (hiLists) should newContain atLeastOneOf ("ho")) (decided by defaultEquality[String])
        }
      }
    }

    object `when used with (contain atLeastOneOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (newContain atLeastOneOf (1, 3, 4))
        atLeast (2, lists) should (newContain atLeastOneOf (1, 3, 4))
        atMost (2, lists) should (newContain atLeastOneOf (2, 3, 4))
        no (lists) should (newContain atLeastOneOf (3, 4, 5))
        no (nils) should (newContain atLeastOneOf (1, 3, 4))
        no (listsNil) should (newContain atLeastOneOf (3, 4, 5))

        val e1 = intercept[TestFailedException] {
          all (lists) should (newContain atLeastOneOf (1, 3, 4))
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 2, " + prettifyArrays(List(2)) + " did not contain at least one of (1, 3, 4) (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(lists)))

        val e2 = intercept[TestFailedException] {
          all (nils) should (newContain atLeastOneOf ("ho", "hey", "howdy"))
        }
        e2.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e2.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e2.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 0, " + prettifyArrays(Nil) + " did not contain at least one of (\"ho\", \"hey\", \"howdy\") (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(nils)))

        val e4 = intercept[TestFailedException] {
          all (listsNil) should (newContain atLeastOneOf (1, 3, 4))
        }
        e4.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e4.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e4.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 2, " + prettifyArrays(Nil) + " did not contain at least one of (1, 3, 4) (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(listsNil)))
      }

      def `should use the implicit Equality in scope` {
        all (hiLists) should (newContain atLeastOneOf ("hi"))
        intercept[TestFailedException] {
          all (hiLists) should (newContain atLeastOneOf ("ho"))
        }
        implicit val ise = invertedStringEquality
        all (hiLists) should (newContain atLeastOneOf ("ho"))
        intercept[TestFailedException] {
          all (hiLists) should (newContain atLeastOneOf ("hi"))
        }
      }
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (newContain atLeastOneOf ("ho"))) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (all (hiLists) should (newContain atLeastOneOf ("hi"))) (decided by invertedStringEquality)
        }
        implicit val ise = invertedStringEquality
        (all (hiLists) should (newContain atLeastOneOf ("hi"))) (decided by defaultEquality[String])
        intercept[TestFailedException] {
          (all (hiLists) should (newContain atLeastOneOf ("ho"))) (decided by defaultEquality[String])
        }
      }
    }

/*
 I purposely don't want to support this syntax:

scala> all (list1s) should newContain (atLeastOneOf (1, 3, 4))
<console>:15: error: org.scalatest.words.NewContainWord does not take parameters
              all (list1s) should newContain (atLeastOneOf (1, 3, 4))
                                             ^

scala> all (list1s) should (newContain (atLeastOneOf (1, 3, 4)))
<console>:15: error: org.scalatest.words.NewContainWord does not take parameters
              all (list1s) should (newContain (atLeastOneOf (1, 3, 4)))
                                              ^

 Reason is that I don't want people putting parentheses between contain and atLeastOneOf, etc. This will not compile.
*/
    object `when used with not contain atLeastOneOf (...) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (toLists) should not newContain atLeastOneOf ("fee", "fie", "foe", "fum")
        val e1 = intercept[TestFailedException] {
          all (toLists) should not newContain atLeastOneOf ("happy", "birthday", "to", "you")
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 0, " + prettifyArrays(List("to")) + " contained at least one of (\"happy\", \"birthday\", \"to\", \"you\") (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(toLists)))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        all (toLists) should not newContain atLeastOneOf ("to", "to", "to", "to")
        intercept[TestFailedException] {
          all (toLists) should not newContain atLeastOneOf ("fee", "fie", "foe", "fum")
        }
      }
      def `should use an explicitly provided Equality` {
        (all (toLists) should not newContain atLeastOneOf ("to", "to", "to", "to")) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (all (toLists) should not newContain atLeastOneOf ("fee", "fie", "foe", "fum")) (decided by invertedStringEquality)
        }
        all (toLists) should not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO ")
        intercept[TestFailedException] {
          (all (toLists) should not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO ")) (after being lowerCased and trimmed)
        }
      }
    }

/*
Interesting, of these three, the last one does happen to compile and run:

scala> all (toLists) should (not (newContain (atLeastOneOf ("fee", "fie", "foe", "fum"))))
<console>:15: error: org.scalatest.words.NewContainWord does not take parameters
              all (toLists) should (not (newContain (atLeastOneOf ("fee", "fie", "foe", "fum"))))
                                                    ^

scala> all (toLists) should not (newContain (atLeastOneOf ("fee", "fie", "foe", "fum")))
<console>:15: error: org.scalatest.words.NewContainWord does not take parameters
              all (toLists) should not (newContain (atLeastOneOf ("fee", "fie", "foe", "fum")))
                                                   ^

scala> all (toLists) should not newContain (atLeastOneOf ("fee", "fie", "foe", "fum"))

The top two don't, but still I don't want to support that in general.
*/
    object `when used with (not contain atLeastOneOf (...)) syntax` {

      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (toLists) should (not newContain atLeastOneOf ("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          all (toLists) should (not newContain atLeastOneOf ("happy", "birthday", "to", "you"))
        }
        e1.failedCodeFileName.get should be ("ListShouldContainAtLeastOneOfSpec.scala")
        e1.failedCodeLineNumber.get should be (thisLineNumber - 3)
        e1.message should be (Some("'all' inspection failed, because: \n" +
                                   "  at index 0, " + prettifyArrays(List("to")) + " contained at least one of (\"happy\", \"birthday\", \"to\", \"you\") (ListShouldContainAtLeastOneOfSpec.scala:" + (thisLineNumber - 5) + ") \n" +
                                   "in " + prettifyArrays(toLists)))
      }
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        all (toLists) should (not newContain atLeastOneOf ("to", "to", "to", "to"))
        intercept[TestFailedException] {
          all (toLists) should (not newContain atLeastOneOf ("fee", "fie", "foe", "fum"))
        }
      }
      def `should use an explicitly provided Equality` {
        (all (toLists) should (not newContain atLeastOneOf ("to", "to", "to", "to"))) (decided by invertedStringEquality)
        intercept[TestFailedException] {
          (all (toLists) should (not newContain atLeastOneOf ("fee", "fie", "foe", "fum"))) (decided by invertedStringEquality)
        }
        all (toLists) should (not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO "))
        intercept[TestFailedException] {
          (all (toLists) should (not newContain atLeastOneOf (" TO ", " TO ", " TO ", " TO "))) (after being lowerCased and trimmed)
        }
      }
    }
  }
}
