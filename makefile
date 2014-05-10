
JFLAGS = -g
SFLAGS = -c -o
JC = javac
SC = swipl
.SUFFIXES: .java .class
.SUFFIXES: .p .o
.java.class:
	$(JC) $(JFLAGS) $*.java

.p.o:
	$(SC) $(SFLAGS) m $*.p

CLASSES = Board.java 
PLGS = mat16.p

default: classes plgs

classes: $(CLASSES:.java=.class)
plgs: $(PLGS:.p=.o)

clean:
	$(RM) *.class
	$(RM) *.p
