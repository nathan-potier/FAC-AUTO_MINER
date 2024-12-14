package Modele;

public class Cell<T> {
    private Position position;
    private T content; // Le contenu de la cellule, générique

    public Cell(Position position, T content) {
        this.position = position;
        this.content = content;
    }

    public Position getPosition() {
        return position;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isEmpty() {
        return content == null;
    }

    @Override
    public String toString() {
        return "Cell[" + position + ", content=" + content + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cell<?> other = (Cell<?>) obj;
        return position.equals(other.position) && 
               (content == null ? other.content == null : content.equals(other.content));
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
