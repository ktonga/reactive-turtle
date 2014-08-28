import java.io.File

val manifest = new java.util.jar.Manifest()
def jar(base: File, dir: String, include: String, out: File) = {
  val files = (base / dir) ** include
  val target = files pair relativeTo(base :: Nil)
  IO.jar(target, out, manifest)
  out
}

lazy val tgJar = TaskKey[(File, File)]("tg-jar", "Jar TG classes and sources into lib folder")

tgJar := {
  val lib = unmanagedBase.value  
  (
    jar((classDirectory in Compile).value, "org", "*.class", lib / "tg.jar"),
    jar((javaSource in Compile).value, "org", "*.java", lib / "tg-src.jar")
  )
}

lazy val tgUnjar = TaskKey[Set[File]]("tg-unjar", "Unjar TG sources into src/main/java folder")

tgUnjar := {
  val jar = unmanagedBase.value / "tg-src.jar"
  val dir = (javaSource in Compile).value
  //Seq(jar, dir)
  IO.unzip(jar, dir)
}


