package tk.michael.project.gui;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created By: Michael Risher
 * Date: 5/11/15
 * Time: 4:10 AM
 */
public class FocusPolicy extends FocusTraversalPolicy {

	private ArrayList<Component> order;

	public FocusPolicy( ArrayList<Component> order ) {
		this.order = new ArrayList<>( order.size() );
		this.order.addAll( order );
	}

	@Override
	public Component getComponentAfter( Container aContainer, Component aComponent ) {
		int idx = ( order.indexOf( aComponent ) + 1 ) % order.size();
		return order.get( idx );
	}

	@Override
	public Component getComponentBefore( Container aContainer, Component aComponent ) {
		int idx = order.indexOf( aComponent ) - 1;
		if ( idx < 0 ) {
			idx = order.size() - 1;
		}
		return order.get( idx );
	}

	@Override
	public Component getFirstComponent( Container aContainer ) {
		return order.get( 0 );
	}

	@Override
	public Component getLastComponent( Container aContainer ) {
		return order.get( order.size() - 1 );
	}

	@Override
	public Component getDefaultComponent( Container aContainer ) {
		return order.get( 0 );
	}
}
