public class Cell {
    private Position position;
    private Object content; // Par exemple, une ressource, une machine, ou null si vide

    public Cell(Position position, Object content) {
        this.position = position;
        this.content = content;
    }

    public Position getPosition() {
        return position;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public boolean isEmpty() {
        return content == null;
    }

    @Override
    public String toString() {
        return "Cell[" + position + ", content=" + content + "]";
    }



    
}
