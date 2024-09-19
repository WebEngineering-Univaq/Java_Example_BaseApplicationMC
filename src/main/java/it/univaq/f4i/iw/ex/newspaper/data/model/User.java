package it.univaq.f4i.iw.ex.newspaper.data.model;



import it.univaq.f4i.iw.framework.data.DataItem;
import java.util.List;

/**
 *
 * @author Giuseppe Della Penna
 */
public interface User extends DataItem<Integer> {

    String getUsername();
    
    void setUsername(String username);

    String getPassword();
    
    void setPassword(String password);
    
    List<String> getRoles();
    
    void setRoles(List<String> roles);

}
