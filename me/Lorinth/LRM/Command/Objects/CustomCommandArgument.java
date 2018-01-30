package me.Lorinth.LRM.Command.Objects;

/**
 * Object used to encapsulate and represent arguments in a command
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
