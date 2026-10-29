public class Subject {

    private int subject_id;
    private String name;
    private int ESPB;

    public Subject(String name, int ESPB) {
        this.name = name;
        this.ESPB = ESPB;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getESPB() {
        return ESPB;
    }

    public void setESPB(int ESPB) {
        this.ESPB = ESPB;
    }
}
