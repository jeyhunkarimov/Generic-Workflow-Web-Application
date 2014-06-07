package com.karimovceyhun.workflow.menu;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;











import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;

import com.karimovceyhun.workflow.utilities.JSFUtil;

@ManagedBean ( name = "userScopedMenuManagedBean" )
@SessionScoped
public class UserScopedMenuManagedBean implements Serializable
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= - 4166242801748468142L;

	private DefaultMenuModel			model;
	private Long				jumpedTaskId;


	@ManagedProperty ( value = "#{menuManagedBean}" )
	private MenuManagedBean		menu;

	public void setMenu( MenuManagedBean menu )
	{
		this.menu = menu;
	}

	public MenuManagedBean getMenu()
	{
		return menu;
	}

	public void setModel( DefaultMenuModel model )
	{
		this.model = model;
	}

	public String getUsername()
	{
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		Boolean superUser = ( Boolean ) request.getSession ().getAttribute ( "superUser" );
		if ( superUser != null && superUser )
		{
			return "Super User";
		}
		else
		{
			return ( String ) request.getSession ().getAttribute ( "name" );
		}
	}

	public DefaultMenuModel getModel()
	{
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		Boolean superUser = ( Boolean ) request.getSession ().getAttribute ( "superUser" );
		DefaultSubMenu submenuNew = new DefaultSubMenu ();
		if ( superUser != null && superUser.booleanValue () )
		{
			for ( MenuElement submenu : menu.getModel ().getElements () )
			{
				if ( submenu instanceof DefaultSubMenu && ( ( DefaultSubMenu ) submenu ).getLabel ().equals ( "Settings" ) )
				{
					model = new DefaultMenuModel ();
					submenuNew.setLabel ( ( ( DefaultSubMenu ) submenu ).getLabel () );
					for ( MenuElement component : ( ( DefaultSubMenu ) submenu ).getElements () )
					{
						submenuNew.getElements ().add ( menuItemClone ( ( DefaultMenuItem ) component , true ) );
					}
				}
			}
		}
		else
		{
			for ( MenuElement submenu : menu.getModel ().getElements() )
			{
				if ( submenu instanceof DefaultSubMenu && ( ( DefaultSubMenu ) submenu ).getLabel ().equals ( "User Menu" ) )
				{
					model = new DefaultMenuModel ();
					submenuNew.setLabel ( ( ( DefaultSubMenu ) submenu ).getLabel () );
					for ( MenuElement component : ( ( DefaultSubMenu ) submenu ).getElements () )
					{
						submenuNew.getElements ().add ( menuItemClone ( ( DefaultMenuItem ) component , true ) );

					}
				}
			}
		}
		for ( MenuElement uic : submenuNew.getElements () )
		{
			model.addElement ( ( DefaultMenuItem ) uic );
		}

		return model;
	}

	/**
	 * Generates same tree with rendered false properties
	 * 
	 * @param model
	 * @param appModel
	 */
	private void cloneModels( DefaultMenuModel model, DefaultMenuModel appModel, boolean forSuperUser )
	{

		// for(DefaultSubMenu submenu : appModel.getSubmenus())
		// {
		// model.getSubmenus().add(submenuClone(submenu, forSuperUser));
		// }
		//
		// for(DefaultMenuItem item : appModel.getMenuItems())
		// {
		// model.getMenuItems().add(menuItemClone(item, forSuperUser));
		// }

		List < MenuElement > submenus = appModel.getElements ();

		for ( MenuElement uiComponent : submenus )
		{
			if ( uiComponent instanceof DefaultSubMenu )
			{
				model.addElement ( submenuClone ( ( ( DefaultSubMenu ) uiComponent ) , forSuperUser ) );
			}
			else if ( uiComponent instanceof DefaultMenuItem )
			{
				model.addElement ( menuItemClone ( ( ( DefaultMenuItem ) uiComponent ) , forSuperUser ) );
			}
		}
	}

	private DefaultSubMenu submenuClone( DefaultSubMenu submenu, boolean forSuperUser )
	{
		DefaultSubMenu submenuNew = new DefaultSubMenu ();
		submenuNew.setRendered ( forSuperUser );
		submenuNew.setLabel ( submenu.getLabel () );

		for ( MenuElement component : submenu.getElements () )
		{
			if ( component instanceof DefaultSubMenu )
			{
				DefaultSubMenu submenuChild = submenuClone ( ( DefaultSubMenu ) component , forSuperUser );
				submenuNew.getElements ().add ( submenuChild );
			}
			else if ( component instanceof DefaultMenuItem )
			{
				submenuNew.getElements ().add ( menuItemClone ( ( DefaultMenuItem ) component , forSuperUser ) );
			}

		}
		return submenuNew;
	}

	private DefaultMenuItem menuItemClone( DefaultMenuItem item, boolean forSuperUser )
	{
		DefaultMenuItem menuItem = new DefaultMenuItem ();

		HttpServletRequest request = ( HttpServletRequest ) FacesContext.getCurrentInstance ().getExternalContext ().getRequest ();

		String page = "http://" + request.getServerName () + ":" + request.getServerPort () + request.getContextPath () + "/";

		menuItem.setValue ( item.getValue () );
		menuItem.setUrl ( page + item.getUrl () );

		menuItem.setRendered ( forSuperUser );

		return menuItem;
	}

	private void generateModel( DefaultMenuModel model, List < String > links )
	{

		// List<DefaultSubMenu> menus = model.getSubmenus();
		// List<DefaultMenuItem> items = model.getMenuItems();
		//
		// for (DefaultSubMenu submenu : menus) {
		// generateModel(submenu, links);
		// }
		//
		// for (DefaultMenuItem item : items) {
		// if (checkSecurity((String) item.getUrl(), links)) {
		// item.setRendered(true);
		// }
		// }

		List < MenuElement > submenus = model.getElements ();

		for ( MenuElement uiComponent : submenus )
		{
			if ( uiComponent instanceof DefaultSubMenu )
			{
				generateModel ( ( DefaultSubMenu ) uiComponent , links );
			}
			else if ( uiComponent instanceof DefaultMenuItem )
			{
				if ( checkSecurity ( ( String ) ( ( DefaultMenuItem ) uiComponent ).getUrl () , links ) )
				{
					( ( DefaultMenuItem ) uiComponent ).setRendered ( true );
				}
			}
		}
	}

	private void generateModel( DefaultSubMenu submenu, List < String > links )
	{
		List < MenuElement > children = submenu.getElements ();
		if ( children != null )
		{
			for ( MenuElement child : children )
			{
				if ( child instanceof DefaultMenuItem )
				{
					DefaultMenuItem item = ( DefaultMenuItem) child;
					if ( checkSecurity ( ( String ) item.getUrl () , links ) )
					{
						item.setRendered ( true );
//						UIComponent parent = item.   .getParent ();
//						while ( parent != null )
//						{
//							parent.setRendered ( true );
//							parent = parent.getParent ();
//						}
					}

				}
				else if ( child instanceof DefaultSubMenu )
				{
					DefaultSubMenu childSubMenu = ( DefaultSubMenu ) child;
					generateModel ( childSubMenu , links );
				}
			}
		}

	}

	public String logout() throws IOException
	{
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		Boolean isAdmin = ( Boolean ) request.getSession ( true ).getAttribute ( "superUser" );
		if ( isAdmin != null && isAdmin )
		{
			request.getSession ().removeAttribute ( "superUser" );
			request.getSession ().removeAttribute ( "isLogined" );
		}
		else
		{
			request.getSession ().removeAttribute ( "isLogined" );
			request.getSession ().removeAttribute ( "id" );
			request.getSession ().removeAttribute ( "name" );
			request.getSession ().removeAttribute ( "superUser" );
		}
		// this.setModel ( null );
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.invalidateSession();
	    ec.redirect("../login.xhtml?faces-redirect=true");

		return "../login.xhtml?faces-redirect=true";
	}

	private boolean checkSecurity( String url, List < String > links )
	{
		if ( links != null )
		{
			for ( String link : links )
			{

				String [ ] urlaction = link.split ( "\\*" );
				String linkUrl = urlaction [ 0 ];
				String action = urlaction [ 1 ];
				if ( action.equals ( "list" ) && url.endsWith ( linkUrl ) )
					return true;
			}
		}

		return false;
	}

	public String jump()
	{
		FacesContext context = FacesContext.getCurrentInstance ();
		HttpServletRequest request = ( HttpServletRequest ) context.getExternalContext ().getRequest ();
		request.getSession ().setAttribute ( "taskId" , Long.valueOf ( jumpedTaskId ) );
		return "taskView";
	}
	
	public void integerValidator()
	{

		 FacesMessage facesMessage = new FacesMessage("Invalid Input", "message");
        FacesContext.getCurrentInstance().addMessage("message", facesMessage);
		
	}
	
	public Long getJumpedTaskId()
	{
		return jumpedTaskId;
	}

	public void setJumpedTaskId( Long jumpedTaskId )
	{
		this.jumpedTaskId = jumpedTaskId;
	}

}
