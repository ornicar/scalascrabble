package scrabble

class LetterBagTest extends ScrabbleTest {

  val lettersOnly = letterBag.letters.map(c => c.letter)

  def getTile(ch: Char) = letterBag.tileSet.get(ch).get

  def letterAppears(ch: Char, bag: LetterBag): Int = {
    bag.letters.foldLeft(0)((acc, let) =>
      if (let.letter == ch) acc + 1 else acc)
  }

  "a letterbag should" should {

    "contain the right numbeEqualTor of each letter" in {
      letterAppears('A', letterBag) must beEqualTo(9)
      letterAppears('B', letterBag) must beEqualTo(2)
      letterAppears('C', letterBag) must beEqualTo(2)
      letterAppears('D', letterBag) must beEqualTo(4)
      letterAppears('E', letterBag) must beEqualTo(12)
      letterAppears('F', letterBag) must beEqualTo(2)
      letterAppears('G', letterBag) must beEqualTo(3)
      letterAppears('H', letterBag) must beEqualTo(2)
      letterAppears('I', letterBag) must beEqualTo(9)
      letterAppears('J', letterBag) must beEqualTo(1)
      letterAppears('K', letterBag) must beEqualTo(1)
      letterAppears('L', letterBag) must beEqualTo(4)
      letterAppears('M', letterBag) must beEqualTo(2)
      letterAppears('N', letterBag) must beEqualTo(6)
      letterAppears('O', letterBag) must beEqualTo(8)
      letterAppears('P', letterBag) must beEqualTo(2)
      letterAppears('Q', letterBag) must beEqualTo(1)
      letterAppears('R', letterBag) must beEqualTo(6)
      letterAppears('S', letterBag) must beEqualTo(4)
      letterAppears('T', letterBag) must beEqualTo(6)
      letterAppears('U', letterBag) must beEqualTo(4)
      letterAppears('V', letterBag) must beEqualTo(2)
      letterAppears('W', letterBag) must beEqualTo(2)
      letterAppears('X', letterBag) must beEqualTo(1)
      letterAppears('Y', letterBag) must beEqualTo(2)
      letterAppears('Z', letterBag) must beEqualTo(1)
    }

    "have the right score for each Tile" in {
      getTile('A').value must beEqualTo(1)
      getTile('B').value must beEqualTo(3)
      getTile('C').value must beEqualTo(3)
      getTile('D').value must beEqualTo(2)
      getTile('E').value must beEqualTo(1)
      getTile('F').value must beEqualTo(4)
      getTile('G').value must beEqualTo(2)
      getTile('H').value must beEqualTo(4)
      getTile('I').value must beEqualTo(1)
      getTile('J').value must beEqualTo(8)
      getTile('K').value must beEqualTo(5)
      getTile('L').value must beEqualTo(1)
      getTile('M').value must beEqualTo(3)
      getTile('N').value must beEqualTo(1)
      getTile('O').value must beEqualTo(1)
      getTile('P').value must beEqualTo(3)
      getTile('Q').value must beEqualTo(10)
      getTile('R').value must beEqualTo(1)
      getTile('S').value must beEqualTo(1)
      getTile('T').value must beEqualTo(1)
      getTile('U').value must beEqualTo(1)
      getTile('V').value must beEqualTo(4)
      getTile('W').value must beEqualTo(4)
      getTile('X').value must beEqualTo(8)
      getTile('Y').value must beEqualTo(4)
      getTile('Z').value must beEqualTo(10)
    }

    "contain 100 letter tiles" in {
      letterBag.letters must have size 100
    }

    def transitionBagProperly(removed: List[Tile], newBag: LetterBag, oldBag: LetterBag): Boolean = {
      removed.foldLeft(true) { (bool, let) =>
        if (bool == false) return false
        else {
          val removedTimes = removed.filter(_ == let) size

          letterAppears(let.letter, newBag) == letterAppears(let.letter, oldBag) - removedTimes
        }

      }
    }

    "properly remove letters from the bag" in {
      val (removed, newBag) = letterBag.remove(3)

      newBag.letters must have size 97
      removed must have size 3

      transitionBagProperly(removed, newBag, letterBag)

      val nextBag = newBag.remove(5)
      nextBag._2.letters must have size 92

      transitionBagProperly(nextBag._1, nextBag._2, newBag)
    }

    "cope with boundary removal conditions" in {
      val (_, bag) = letterBag.remove(95)
      val (removed, boundaryBag) = bag.remove(7)

      removed.size must beEqualTo(5)
      boundaryBag.letters must beEmpty

    }

    def exchangesProperly(exchanged: List[Letter], originalBag: LetterBag): Unit = {
      val (received, newbag) = letterBag.exchange(exchanged).get
      exchanged.foreach {
        c =>
          val newAmount = letterAppears(c.letter, newbag)

          newAmount must beEqualTo((letterAppears(c.letter, letterBag) + exchanged.filter(_ == c).size)
            - received.filter(_ == c).size)

      }

      newbag.size must beEqualTo(letterBag.size)
    }

    def strToLetters(str: String): List[Letter] = str.toList map (c => letterBag.tileSet.get(c).get)

    "properly exchange letters" in {
      val exchangedWithSingle = strToLetters("ABCDE")
      exchangesProperly(exchangedWithSingle, letterBag)

      val exchangedWithDoubleLetters = strToLetters("AABCDD")
      exchangesProperly(exchangedWithDoubleLetters, letterBag)

    }

    "fail to exchange when there is not enough letters in the bag to exchange for" in {
      val (_, smallBag) = letterBag.remove(98)

      smallBag.exchange(strToLetters("AABCDD")) must beNone

    }

    "keep track of the letter bag's size correctly" in {
      val (_, newBag) = letterBag.remove(6)
      newBag.size must beEqualTo(94)

      val (_, emptyBag) = newBag.remove(100)
      emptyBag.size must beEqualTo(0)

      val exchangedWithSingle = strToLetters("ABCDE")
      val (_, exchBag) = newBag.exchange(exchangedWithSingle).get
      
      exchBag.size must beEqualTo(94)
    }

  }

}