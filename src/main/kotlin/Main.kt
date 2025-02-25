import java.io.File

fun main(args: Array<String>) {
//  val workingDirectory= File(".").canonicalPath
  val workingDirectory=File("/google/src/cloud/dmail/pristine/google3/third_party/devsite/firebase/en/docs")
  val outputFile=File("/usr/local/google/home/dmail/Documents/FB_inventory.txt")
  val rawFileList=workingDirectory.listFiles()
  val directoryList=getDirList(rawFileList)
  val listOfMDFiles=getMDFiles(directoryList)
//  outputFileAttributes(listOfMDFiles,outputFile)
  concatFiles(directoryList,listOfMDFiles)
}

fun getDirList(rawFileList:Array<File>):List<File> {
  val usableList=mutableListOf<File>()
  for (file in rawFileList)
    if (file.isDirectory && !file.name.startsWith(".")) {
      usableList.add(file)
    }
  return usableList
}
fun getMDFiles(directoryList:List<File>):MutableList<MutableList<File>> {
  val fileMap=mutableListOf<MutableList<File>>()
  for (directory in directoryList) {
    val currentDirectory = directory.walkTopDown()
    val individualDirectory=mutableListOf<File>()

    for (file in currentDirectory) {
        if (file.name.endsWith(".md"))
          individualDirectory.add(file)
    }
    fileMap.add(individualDirectory)
  }
  return fileMap
}

fun outputFileAttributes(fileMap:MutableList<MutableList<File>>,
                        outputFile:File) {
  var jumboCounter=0
for (directory in fileMap) {
  var totalSize=0L
  for (file in directory) {
    println (file.canonicalPath.substringAfter("docs")+" "+file.length())
    totalSize+=file.length()
  }
  println("\nTotal number of bytes in directory: ${totalSize}\n")
  if (totalSize > 500000)
    jumboCounter++
}
println ("a total of ${jumboCounter} jumbos.")
}

 fun concatFiles (directories:List<File>,
                  filesByDirectory:MutableList<MutableList<File>>) {
   var directoryIndex=-1
   // Use this to iterate through directory names


     for (eachDirectory in filesByDirectory) {
       directoryIndex++
       val concatFile=File(directories[directoryIndex].toString()+"_concat.md")
       val aggregatedText = StringBuilder()
       var totalSize=0L

       for (eachFile in eachDirectory) {
         totalSize += eachFile.length()
         if (totalSize > 500000) {
           println("500k limit exceeded. ${directories[directoryIndex]} truncated.")
//           break
         }
         eachFile.forEachLine { aggregatedText.append(it).append("\n")  }
       }
       concatFile.writeText(aggregatedText.toString())
       totalSize=0
     }
 }