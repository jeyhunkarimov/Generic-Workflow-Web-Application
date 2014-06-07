package com.karimovceyhun.workflow.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.DualListModel;

import com.karimovceyhun.workflow.data.Project;
import com.karimovceyhun.workflow.data.User;
import com.karimovceyhun.workflow.data.Workflow;
import com.karimovceyhun.workflow.interfaces.IProjectService;
import com.karimovceyhun.workflow.interfaces.IUserService;
import com.karimovceyhun.workflow.services.ServiceFinder;
import com.karimovceyhun.workflow.utilities.JSFUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

@ManagedBean ( name = "projectListingManagedBean" )
@ViewScoped
public class ProjectListingManagedBean extends ListingManagedBean < Project > implements Serializable
{
	private static final long		serialVersionUID	= 4426815554605967048L;
	private DualListModel < User >	users				= new DualListModel < User > ();
	private DualListModel < User >	managers			= new DualListModel < User > ();
	private List < User >			userListSource;
	private Boolean					isAdmin				= false;
	private User					currentUser;
	// private User tete;

	private List < Project >		projects			= null;

	public ProjectListingManagedBean()
	{
		super ( "ProjectBean" );
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		try
		{
			isAdmin = ( Boolean ) request.getSession ().getAttribute ( "superUser" );
		}
		catch ( Exception e )
		{
			// TODO: handle exception
		}
		if ( isAdmin != null )
		{
			if ( ! isAdmin )
			{
				try
				{
					Long id = ( Long ) request.getSession ().getAttribute ( "id" );
					currentUser = ( ( IUserService ) ServiceFinder.findBean ( "UserService" ) ).findUser ( id );
				}
				catch ( Exception e )
				{
					// TODO: handle exception
				}
			}
			loadData ();
		}
	}

	@Override
	public SelectItem [ ] getListOfColumns()
	{
		SelectItem [ ] items = new SelectItem [ 2 ];
		items [ 0 ] = new SelectItem ( "String|name" , "Full Name" );
		items [ 1 ] = new SelectItem ( "Long|id" , "Id" );
		return items;
	}

	/**
	 * loads data
	 */
	@Override
	protected void loadData()
	{
		if ( isAdmin )
		{
			projects = getProjectService ().list ( Project.class , getDetachedCriteria () );
		}
		else
		{
			projects = getProjectService ().getMyProjects ( currentUser.getId () );
		}
	}

	public DetachedCriteria getDefaultCriteria()
	{
		return DetachedCriteria.forClass ( Project.class );
	}

	@Override
	public DetachedCriteria getDetachedCriteria()
	{
		DetachedCriteria detachedCriteria = getDefaultCriteria ();

		if ( getSelectedColumn () == null || getSelectedColumn ().trim ().equals ( "" ) || getInput () == null || getInput ().trim ().equals ( "" ) )

			return detachedCriteria;

		else
		{
			String [ ] parsedColumn = getSelectedColumn ().split ( "\\|" );

			String type = parsedColumn [ 0 ];
			String query = parsedColumn [ 1 ];

			Object inputObject = null;

			if ( type.equals ( "String" ) || type.equals ( "AutoComplete" ) )
			{

				inputObject = new String ( getInput () );

				if ( query.indexOf ( ":" ) > 0 )
				{

					String parts[] = query.split ( "\\:" );

					DetachedCriteria next = detachedCriteria.createCriteria ( parts [ 0 ] );

					for ( int i = 1 ; i < parts.length - 1 ; i ++ )
						next = next.createCriteria ( parts [ i ] );

					next.add ( Restrictions.ilike ( parts [ parts.length - 1 ] , ( String ) inputObject , MatchMode.ANYWHERE ) );

				}
				else
				{
					detachedCriteria.add ( Restrictions.ilike ( query , ( String ) inputObject , MatchMode.ANYWHERE ) );
				}

			}
			else if ( type.startsWith ( "Enum" ) )
			{

				String typeAnalysis[] = type.split ( "\\:" );
				String orjType = typeAnalysis [ typeAnalysis.length - 1 ];

				if ( orjType.equals ( "Long" ) )
					inputObject = new Long ( getInput () );
				else if ( orjType.equals ( "String" ) )
					inputObject = getInput ();
				else
					inputObject = new Integer ( getInput () );

				if ( query.indexOf ( ":" ) > 0 )
				{
					String parts[] = query.split ( "\\:" );

					DetachedCriteria next = detachedCriteria.createCriteria ( parts [ 0 ] );

					for ( int i = 1 ; i < parts.length - 1 ; i ++ )
						next = next.createCriteria ( parts [ i ] );

					next.add ( Restrictions.eq ( parts [ parts.length - 1 ] , inputObject ) );

				}
				else
				{
					detachedCriteria.add ( Restrictions.eq ( query , inputObject ) );
				}
			}

			return detachedCriteria;
		}
	}

	public void setProjects( List < Project > projects )
	{
		this.projects = projects;
	}

	public List < Project > getProjects()
	{

		if ( projects == null )
		{
			loadProjects ();
		}
		return projects;
	}

	private void loadProjects()
	{

		setProjects ( getProjectService ().list ( Project.class ) );
	}

	public IProjectService getProjectService()
	{
		return ( IProjectService ) ServiceFinder.findBean ( "ProjectService" );
	}

	@Override
	protected void doBeforeSave( List < FacesMessage > messages )
	{
		List < User > targetUsers = users.getTarget ();
		getBean ().setUsers ( new HashSet < User > ( targetUsers ) );
		// tete.getEmail();
		// getBean().setManager(tete);
	}

	@Override
	protected void initializeBean()
	{
		String id = JSFUtil.getRequestParameter ( "id" );

		setBean ( getProjectService ().findProject ( new Long ( id ) ) );
		editProjectUsersPicklist ();
		// tete = getBean().getManager();
	}

	public DualListModel < User > getUsers()
	{
		return users;
	}

	public void setUsers( DualListModel < User > users )
	{
		this.users = users;
	}

	public void editProjectUsersPicklist()
	{
		getUserListSource ();
		List < User > userListTarget = new ArrayList < User > ( getBean ().getUsers () );
		userListSource.removeAll ( userListTarget );
		users = new DualListModel < User > ( userListSource , userListTarget );
	}

	public void initializeNewProjectUsersPicklist()
	{
		addNew ();
		userListSource = getUserService ().list ( User.class , DetachedCriteria.forClass ( User.class ) );
		users = new DualListModel < User > ( userListSource , new ArrayList < User > () );
	}

	public IUserService getUserService()
	{
		return ( IUserService ) ServiceFinder.findBean ( "UserService" );
	}

	public DualListModel < User > getManagers()
	{
		return managers;
	}

	public void setManagers( DualListModel < User > managers )
	{
		this.managers = managers;
	}

	public List < User > completeUser( String query )
	{
		List < User > suggestions = new ArrayList < User > ();

		/*
		 * for(User e : getUserListSource()) {
		 * if(e.getFullName().toLowerCase().contains(query.toLowerCase()))
		 * suggestions.add(e); }
		 */
		// DetachedCriteria det = DetachedCriteria.forClass(User.class);
		// det.createCriteria("user").add(Restrictions.like("name", "%"+ query +
		// "%"));
		// det.add(Restrictions.like("name", "%"+ query + "%"));
		// det.setProjection(Projections.property("name"));
		// suggestions = getUserService().list(User.class,det);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass ( User.class );
		detachedCriteria.add ( Restrictions.like ( "fullName" , "%" + query + "%" ) );
		suggestions = getUserService ().list ( User.class , detachedCriteria );

		return suggestions;
	}

	public List < User > getUserListSource()
	{
		userListSource = getUserService ().list ( User.class , DetachedCriteria.forClass ( User.class ) );
		return userListSource;
	}

	public void setUserListSource( List < User > userListSource )
	{
		this.userListSource = userListSource;
	}

	public Boolean getIsAdmin()
	{
		return isAdmin;
	}

	public void setIsAdmin( Boolean isAdmin )
	{
		this.isAdmin = isAdmin;
	}

	public User getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser( User currentUser )
	{
		this.currentUser = currentUser;
	}

	@Override
	public String delete() {
		
//		// check workflow.
//		
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass ( Workflow.class );
//		
//		detachedCriteria.setFetchMode("projects", FetchMode.JOIN);
//		detachedCriteria.createCriteria("projects", "p", DetachedCriteria.LEFT_JOIN).add( Restrictions.eq("p.id", getBean().getId()));
//		List<Workflow> workflows = getCommonService().list(Workflow.class, detachedCriteria);
//		if( !workflows.isEmpty() )
//		{
//			for (Workflow workflow : workflows) {
//				
//				while (workflow.getProjects().iterator().hasNext()) {
//					Project project = (Project) workflow.getProjects().iterator().next();
//					if( project.equals(getBean()) )
//					{
//						workflow.getProjects().remove(project);
//						break;
//					}
//				}
//				getCommonService().save(workflow);
//			}
//		}
		
		// empty project
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass ( Project.class );
		
		detachedCriteria.setFetchMode("users", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.eq("id", getBean().getId()));
		
		List<Project> projects = getCommonService().list(Project.class, detachedCriteria);
		
		Project pro = projects.get(0);
		
		pro.setManager(null);
		
		pro.getUsers().clear();
		
		getCommonService().save(pro);
		
		setBean( this.getCommonService().find(this.getBean().getClass(),new Long(pro.getId())) );
		return super.delete();
	}
}
