
JFLAGS = -g
SFLAGS = -c -o
JC = javac
SC = swipl
.SUFFIXES: .java .class
.SUFFIXES: .p .o
.java.class:
	$(JC) $(JFLAGS) $*.java

.p.o:
	$(SC) $(SFLAGS) m4 $*.p

CLASSES = Board4.java 
PLGS = mat4.p

default: classes plgs

classes: $(CLASSES:.java=.class)
plgs: $(PLGS:.p=.o)

clean:
	$(RM) *.class
	$(RM) *.p
