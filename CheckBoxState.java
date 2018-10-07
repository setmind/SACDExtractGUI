public class CheckBoxState{
    public boolean selected;
    public boolean enabled;


    public CheckBoxState(boolean s, boolean e){
        this.selected = s;
        this.enabled = e;
    }
    public CheckBoxState(){
        this.selected = false;
        this.enabled = false;
    }
}
