package me.Lorinth.LRM.Command.Objects;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class CustomCommandArgument {
    private String Label;
    private String Description;
    private boolean IsRequired;

    public CustomCommandArgument(String label, String description, boolean isRequired){
        Label = label;
        Description = description;
        IsRequired = isRequired;
    }

    public String getLabel(){
        return Label;
    }

    public String getLabelWithTag(){
        if(isRequired())
            return "<" + getLabel() + ">";
        else
            return "[" + getLabel() + "]";
    }

    public String getDescription(){
        return Description;
    }

    public boolean isRequired(){
        return IsRequired;
    }
}
