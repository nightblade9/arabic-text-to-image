rd /s /q output
mkdir output

"C:\Program Files\Java\jdk1.8.0_162\bin\javac.exe" -encoding UTF-8 -cp "gson-2.8.2.jar;"  TextToGraphics.java Word.java
java -cp ".;gson-2.8.2.jar" TextToGraphics
