package scalate

sealed trait Slide{
  def title:String
}

case class Title(title: String, subtitle:String, author:String, email:String, twitter:String, notes:String) extends Slide

case class Bullet(title: String, list:Seq[Point],notes:String ) extends Slide

case class Headline(title: String, subtitle: String, body:String, notes:String) extends Slide

case class CodeExample(title: String, code:String) extends Slide

case class Point(text: String)

sealed trait SlidesTrait[+A] {
  def slides: Seq[A]
}
case class Slides[Slide](slides: Seq[Slide])
