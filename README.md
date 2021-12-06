## Graphing Calculator + OpenGL GUI

### Setup

In IntelliJ go `File -> Project Structure -> Libraries -> + -> Java` and select the lwjgl folder. Then Ok.

Then still in Project Structure go to `Modules -> course-project... -> Dependencies -> + -> Library` and select lwjgl. Then Ok.

Then add the lwjgl libraries to classpath by right-clicking on any of the GL import statments.

### Running

Build the project. Then run Frontend.CommandLineInterface with some arguments, ex. `-eq "mandel(x,y) = 0" -graph GRAYSCALE -interactive 1`

Click and drag to pan. Zoom is also supported.

----


Includes image I/O, a 2D implicit function grapher, and an experimental 3D raymarcher.

![Sample 2D](https://raw.githubusercontent.com/lz-uoft/course-project-8-musketeers/main/sampleOut2D.png)
![Sample 3D](https://raw.githubusercontent.com/CSC207-UofT/course-project-8-musketeers/main/sampleOutMandel.png)
<img src="https://raw.githubusercontent.com/CSC207-UofT/course-project-8-musketeers/main/Raymarch.png" width=256 height=256>
