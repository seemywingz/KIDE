# KIDE

Build Instructions


<h1>Setting Up the Environment</h1>

<h2>Download and install the latest JDK</h2>
KIDE was built with JDK 7_u75, but JDK 8 and its latest update will work as well.

<h3>Windows</h3>
You can find the latest JDK <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html" target="blank">here</a>. 

<h3>Linux</h3>
I used Oracle JDK for this build of KIDE, but earlier versions used Open JDK. At the time of this post the latest version of Open JDK is 7 and Oracle JDK is 8. Both will work with the committed code and installation for both can be found <a href="https://help.ubuntu.com/community/Java" target="blank">Here</a>.

<h2>Download and install GIT for your system.</h2>
 Git is a free and open source distributed version control system. If you are unfarmiliar with how to use some of the more basic functions of git check out their <a href="https://try.github.io/levels/1/challenges/1" target="blank">Code School</a>.
<h3>Windows</h3>
<a href="http://git-scm.com/downloads" target="blank">Here</a> are the latest git versions.<br>
When installing tick the checkbox to add "git bash here" to the windows right click menu.
<h3>Linux/Mac</h3>
You can follow <a href="http://git-scm.com/book/en/v2/Getting-Started-Installing-Git" target="blank">These Instructions</a> 

<h2>Download and install IntelliJ Idea.</h2>
Because IntelliJ is pretty buch the best Java IDE out there and because if you stare at eclipse for one more minute your eyeballs will melt!
<h3>Windows</h3>
<a href="https://www.jetbrains.com/idea/download/" target="blank">Download</a> and install.
<h3>Linux/Mac</h3>
<a href="https://www.jetbrains.com/idea/download/" target="blank">Download</a> and extract the .tar.
then create a folder in a known spot, copy the idea folder there and create a simlink.<br>
`~$ mkdir /path/to/intellij`<br>
`~$ cp /path/to/extracted/ideaFolder /path/to/intellij`<br>
`~$ sudo ln -s /path/to/intellij/bin/idea.sh /usr/bin/idea`<br>
Test to see if you can run the program by typing
`~$ idea`<br>

