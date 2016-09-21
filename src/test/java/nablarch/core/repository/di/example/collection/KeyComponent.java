package nablarch.core.repository.di.example.collection;

public class KeyComponent {

    private String id;
    private String lang;

    public String getValue1() {
        return getId();
    }
    public String getId() {
        return id;
    }
    public void setValue1(String value1) {
        setId(value1);
    }
    public void setId(String value1) {
        this.id = value1;
    }
    public String getLang() {
        return lang;
    }
    public void setLang(String value2) {
        this.lang = value2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lang == null) ? 0 : lang.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KeyComponent other = (KeyComponent) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lang == null) {
            if (other.lang != null)
                return false;
        } else if (!lang.equals(other.lang))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "KeyComponent [id=" + id + ", lang=" + lang + "]";
    }
    
    
}
