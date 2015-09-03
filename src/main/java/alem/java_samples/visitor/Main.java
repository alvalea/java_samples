package alem.java_samples.visitor;

import java.util.ArrayList;
import java.util.List;

interface IVisitor {
    
    void visit(IFigure e);
    
    void visitTriangle(Triangle t);
    
    void visitCircle(Circle c);
    
}

interface IFigure {
    
    void accept(IVisitor visitor);
    
}

class Triangle implements IFigure {

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitTriangle(this);
    }
    
}

class Circle implements IFigure {

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitCircle(this);
    }
    
}

class PrintVisitor implements IVisitor{

    @Override
    public void visit(IFigure e) {
        e.accept(this);
    }

    @Override
    public void visitTriangle(Triangle t) {
        System.out.println("triangle");
    }

    @Override
    public void visitCircle(Circle c) {
        System.out.println("circle");
    }
    
}

class NoVisitor {

    static void print(List<IFigure> figures) {
        figures.stream().forEach((figure) -> {
            if (figure instanceof Triangle) {
                System.out.println("triangle");
            } else if (figure instanceof Circle) {
                System.out.println("circle");
            }
        });
    }
    
}

public class Main {

    static void noVisitor(List<IFigure> list) {
        System.out.println("NO VISITOR");
        
        NoVisitor.print(list);
        
        System.out.println("");
    }
    
    static void visitor(List<IFigure> list) {
        System.out.println("VISITOR");
        
        PrintVisitor visitor = new PrintVisitor();
        for(IFigure element : list) {
            visitor.visit(element);
        }
        
        System.out.println("");
    }
    
    static List<IFigure> createData() {
        List<IFigure> list = new ArrayList<>();
        list.add(new Triangle());
        list.add(new Circle());
        list.add(new Circle());
        list.add(new Circle());
        list.add(new Triangle());
        list.add(new Circle());
        return list;
    }
    
    public static void main(String[] args) {
        
        List<IFigure> list = createData();
        
        noVisitor(list);
        
        visitor(list);
        
    }
}
