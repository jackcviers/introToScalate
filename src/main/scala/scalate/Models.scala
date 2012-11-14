package scalate

sealed trait Slide{
  def title: String
  def index: Int
}

case class Title(index: Int, title: String, subtitle: String, author: String, email: String, twitter: String, notes: String) extends Slide

case class Bullet(index: Int, title: String, list: Seq[Point], notes: String ) extends Slide

case class References(index: Int, title: String, list: Seq[ReferenceLink], notes: String) extends Slide

case class Headline(index: Int, title: String, subtitle: String, body: String, notes: String) extends Slide

case class CodeExample(index: Int, title: String, code: String, notes: String) extends Slide

case class Point(text: String)

case class ReferenceLink(linkText: String, linkUri: String)

case class Person(name: String, location: String) 

sealed trait SlidesTrait[+A] {
  def slides: Seq[A]
}

case class Slides(slides: Seq[Slide]) extends SlidesTrait[Slide]
