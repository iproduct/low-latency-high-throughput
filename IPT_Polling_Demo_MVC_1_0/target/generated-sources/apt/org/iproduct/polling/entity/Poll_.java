package org.iproduct.polling.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.iproduct.polling.entity.Alternative;
import org.iproduct.polling.entity.PollStatus;

@Generated(value="EclipseLink-2.5.0.v20130425-rNA", date="2015-11-19T16:57:08")
@StaticMetamodel(Poll.class)
public class Poll_ { 

    public static volatile SingularAttribute<Poll, String> question;
    public static volatile SingularAttribute<Poll, Date> start;
    public static volatile ListAttribute<Poll, Alternative> alternatives;
    public static volatile SingularAttribute<Poll, Date> end;
    public static volatile SingularAttribute<Poll, Long> id;
    public static volatile SingularAttribute<Poll, String> title;
    public static volatile SingularAttribute<Poll, PollStatus> status;

}