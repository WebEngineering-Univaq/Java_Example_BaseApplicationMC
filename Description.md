# Guide to the code of this example (*SimpleDataModel*)

These classes are also present in other examples like
BaseApplication_MC and BaseApplication_MVC.

## Common Framework
### Package it.univaq.f4i.iw.framework.data

#### Interface *DataItem*
Base interface from which to derive your own data classes.
Declares common properties *Key* and *Version*.

#### Class *DataItemImpl* implements DataItem
Base class from which to derive your own data classes.
Manages the common properties *Key* and *Version*.

#### Interface *DataItemProxy*
Basic interface from which to derive proxies for your data classes.
Declares the *modified* flag and other low-level characteristics to 
be included in all the data model proxies.

#### Class *DAO*
Base class from which to derive the DAOs for data objects.
Implements the basic structures to manage the connection and the dataLayer.

#### Class *DataLayer*
Base class that allows access to all the DAOs registered in the system,
to the object cache, to the DataSource, and to the DBMS connection. 
It is responsible for creating and initializing all the DAOs, 
obtaining the database connection (*init*), and releasing it (*destroy*). 
An instance of this class should be created for each call to 
the *service* of each Servlet.

## Application-specific data model

### Package it.univaq.f4i.iw.ex.newspaper.data.model

#### Interface *Article* implements DataItem
Declares the getters and setters for specific data of this data object, 
inheriting Key and Version from DataItem.

### Package it.univaq.f4i.iw.ex.newspaper.data.model.impl

#### Class *ArticleImpl* extends DataItemImpl implements Article
Basic implementation of the corresponding data object, 
inherits from DataItemImpl the code for managing Key and Version.

### Package it.univaq.f4i.iw.ex.newspaper.data.model.impl.proxy

#### Class *ArticleProxy* extends ArticleImpl implements DataItemProxy
Adds to the base implementation of ArticleImpl the management 
of lazy loading and the modification flag, the latter specified 
in DataItemProxy.

### Package it.univaq.f4i.iw.ex.newspaper.data.dao

#### Interface *ArticleDAO*
Declares all the data access methods usable in the controller and implemented
in the model.

### Package it.univaq.f4i.iw.ex.newspaper.data.dao.impl

#### Class *ArticleDAO_MySQL* extends DAO implements ArticleDAO
Implements the methods of the corresponding DAO. 
Shows how to implement data object caching and optimistic locking.

#### Class *NewspaperDataLayer* extends DataLayer
Registers in DataLayer the DAOs specific to the data model in use.