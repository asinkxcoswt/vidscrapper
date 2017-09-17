package mmletmesee.utils

class With[T](val value: T) {
  def ifSome(expression: T => Unit): With[T] = { if (value != null.asInstanceOf[T]) expression(value); this }
  def ifNone(expression: => Unit): With[T] = { if (value == null.asInstanceOf[T]) expression; this }
  def by(expression: T => Unit): With[T] = { expression(value); this }
}

object With {
  implicit def apply[T](obj: T) = new With(obj)
  implicit def deApply[T](wth: With[T]) = wth.value
}
