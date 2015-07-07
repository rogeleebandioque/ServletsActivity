package hibernateactivity.core.dao.actions;

import hibernateactivity.core.dao.Command;
import hibernateactivity.core.model.Person;
import org.hibernate.Session;
import java.util.*;
import org.hibernate.*;
import org.hibernate.sql.JoinType;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;

public class ListPerson implements Command {

    private Session session;
    private String listBy;
    private String orderBy;

    public ListPerson(String listBy, String orderBy) {
        this.listBy = listBy;
        this.orderBy = orderBy;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Object execute() {
        String sql = null;
        List<Person> persons = null;  
        listBy = listBy.replace(" ", "_").toLowerCase();
           if(!listBy.equals("last_name")) {
                Criteria cr = session.createCriteria(Person.class); 
                if(orderBy.equals("desc")) {
                    cr.addOrder(Order.desc(listBy));
                } else { 
                    cr.addOrder(Order.asc(listBy));    
                }
                cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                persons = cr.list();
            } else {              
                if(orderBy.equals("asc")){
                    sql = "FROM Person ORDER BY names.last_name ASC";             
                } else {
                    sql = "FROM Person ORDER BY names.last_name DESC";          
                }
                persons  = session.createQuery(sql).list();
            }

        /*Criteria criteria = session.createCriteria(Person.class);
        criteria.setProjection(Projections.projectionList()
        .add(Projections.property("id"), "id")
        .add(Projections.property("names"), "names")
        .add(Projections.property("grade"), "grade")
        .add(Projections.property("date_hired"), "date_hired")
        .add(Projections.property("contact"), "contact"));
        persons = criteria.setResultTransformer(Transformers.aliasToBean(Person.class)).list();
        */

        return persons;
    }
}
