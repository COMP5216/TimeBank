package comp5216.sydney.edu.au.timebank.model;
import com.google.firebase.firestore.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Task {

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_TIME_COIN = "time_coin";


    private String name;
    private String title;
    private String description;
    private String photo;
    private int time_coin;

    public Task(){}

    public Task(String name,String title,String description,String photo,int time_coin){
        this.name=name;
        this.description=description;
        this.photo=photo;
        this.time_coin=time_coin;
        this.title=title;
    }


    public String getName() {return  name;}

    public void setName(String name){this.name=name; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getTime_coin() { return time_coin; }

    public void setTime_coin(int time_coin) {
        this.time_coin = time_coin;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", photo='" + photo + '\'' +
                ", time_coin=" + time_coin +
                '}';
    }
}
