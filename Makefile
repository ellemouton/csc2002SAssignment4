#CSC2002S Assignment4 Makefile
#Elle Mouton
#22/09/2018

JAVAC=/usr/bin/javac
JAVA = java

default: Tree.class Land.class ForestPanel.class TreeGrow.class SunData.class Simulator.class SunThread.class

Tree.class: Tree.java
	$(JAVAC) Tree.java

Land.class: Land.java
	$(JAVAC) Land.java

ForestPanel.class: ForestPanel.java
	$(JAVAC) ForestPanel.java

TreeGrow.class: TreeGrow.java
	$(JAVAC) TreeGrow.java

SunData.class: SunData.java
	$(JAVAC) SunData.java	

Simulator.class: Simulator.java
	$(JAVAC) Simulator.java

SunThread.class: SunThread.java
	$(JAVAC) SunThread.java



clean:
	rm  *.class

	
run:
	$(JAVA) SunCalc ${ARGS}