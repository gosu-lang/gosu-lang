/** 
 * @fileoverview
 * Dynamic Folder Tree
 * Generates DHTML tree dynamically (on the fly).
 * License: BSD. 
 *    See details at http://www.opensource.org/licenses/bsd-license.php
 * 
 * Copyright (c) 2004, 2005, 2006, 2008
 *    Vinicius Cubas Brand, Raphael Derosso Pereira, Frank Alcantara, Benoît VAN BOGAERT (as Macq Electronique sub-contractor)
 * {viniciuscb,raphaelpereira,frankalcantara, bvanbogaert} at users.sourceforge.net
 * All rights reserved.
 */

/**
 * Creates the default dFTree layout.
 * It is responsible to create and maintain HTML objects. 
 * @private
 */ 
function dFTreeDefaultLayout(arrayProps) {
	// Mandatory fields
    /**
     * Control the behaviour of the tree. 
     * If true and when a node is opened, the other node's brothers are closed.
     * This field is mandatory.
     * @type boolean
     * @public
     */
	this.closeBrothers = false;
	
	// Optional fields
	
	// Private fields
	
	for (i in arrayProps)
	{
		if (i.charAt(0) != '_')
		{
			eval('this.'+i+' = arrayProps[\''+i+'\'];');
		}
	}
}

/** 
 * Insert in the document the HTML code for the root of the tree
 * @param {dFTree} tree The reference tree
 * @private
 */
dFTreeDefaultLayout.prototype._draw_tree = function(tree) {
	if (!getObjectById("dftree_"+tree.name))
	{
		document.write('<table class="root" id="dftree_'+tree.name+'" cellspacing="0" cellpadding="0"></table>');
	}
}

/** 
 * Compute the rowIndex interval in the tree table related to the children's of the given node.
 * @param {dNode} node The reference node
 * @return an object representing the row index interval of the given node children
 * Attribute interval.startIndex is the row Index of the first child
 * Attribute interval.count is the count of rows (including the children of children)
 * @private
 */
dFTreeDefaultLayout.prototype._get_children_interval = function(node) {
	var startIndex = 0;
	var endIndex = 0;
	
	var nodeRow = getObjectById("n"+node.id);
	if (nodeRow) {
		startIndex = nodeRow.rowIndex + 1;
	
		var table = nodeRow.parentNode.parentNode;
		if (table) {
			endIndex = table.rows.length;
			var parent = node;
			var quit = false;
			while (!quit && parent)
			{
				var list = null;
				if (parent._parent) {
					list = parent._parent._children;
				} else {
					list = node._myTree._roots;
				}
				// Search just following brother
				var parentIndex = node._myTree._searchNodeIn(parent.id, list);
				if (parentIndex < list.length - 1) {
					var uncleRow = getObjectById("n"+list[parentIndex+1].id);
					if (uncleRow) {
						endIndex = uncleRow.rowIndex;
						quit = true;
					}
				}
				parent = parent._parent;
			}
		}
	}
	
	return { startIndex: startIndex, count:(endIndex - startIndex) };
}

/**
 * Create the HTML objects to draw the given node
 * @param {dNode} node The reference node
 * @private
 */
dFTreeDefaultLayout.prototype._draw_node = function(node)
{
	var str;
	var div;
	var myPlus = node._properPlus();
	var myIcon = node._properIcon();
	var myPlusOnClick = node._myTree.name+'.getNodeById(\''+node.id+'\').changeState();';
	var captionOnClickEvent = "";

	var plusEventHandler = function(){
		eval(myPlusOnClick);
	}

	var captionEventHandler = function(){
		eval(captionOnClickEvent);
	}

	//FIXME put node in a separate function, as node will be necessary in 
	//various parts

	captionOnClickEvent = node._myTree.name+'.getNodeById(\''+node.id+'\')._select(); ';
	if (node.onClick) //FIXME when onclick && url
	{
		captionOnClickEvent += node.onClick;
	}
	else if (node.url && node.target)
	{
		captionOnClickEvent += 'window.open(\''+node.url+'\',\''+node.target+'\')';
	}
	else if (node.url)
	{
		captionOnClickEvent += 'window.location=\''+node.url+'\'';
	}
	
	var table = getObjectById("dftree_"+node._myTree.name);
	
	if (table) {
		var rowIndex = table.rows.length; // Row index where to insert the node
		var is_shown = true; 
		if (node._parent != null) {
			var interval = this._get_children_interval(node._parent);
			rowIndex = interval.startIndex + interval.count;
			is_shown = node._parent._io;
		}
		
		// Compute current node level, 
		var level = node._get_level();
		
		// Possibly adjust the colspan for other nodes 
		var must_clean_table = false;
		if (typeof(node._myTree._levelMax) == "undefined") {
			node._myTree._levelMax = level;
			must_clean_table = true;
		} else if (node._myTree._levelMax < level) {
			node._myTree._levelMax = level;
			must_clean_table = true;
		}

		var objN = table.insertRow(rowIndex);
		objN.id = 'n'+node.id;
		objN.className = 'son';
		if (!is_shown) { 
			objN.style.display = "none";
		}
		
		for(i=0; i<level; i++) {
			var c = objN.insertCell(i);
			c.className = "tc";
		}
		this._refresh_parent_vertical_lines(node, level, objN);
		
		var index = level;
		
		//The cell that holds the plus/minus sign
		var objP = objN.insertCell(index++);
		objP.id = 'p'+node.id;
		objP.className = 'plus';
		objP.onclick = plusEventHandler;
		this._update_vertical_lines(node, objP);
		objP.innerHTML = myPlus;

		//The cell that holds the icon
		if (node._myTree.useIcons) {
			objI = objN.insertCell(index++);
			objI.id = 'i'+node.id;
			objI.className = "icon";
			// objI.onclick = captionEventHandler;
			if (node._myTree.useIcons)
				objI.innerHTML = myIcon;
			else
				objI.innerHTML = "";
		}

		//The cell that holds the label/caption
		objL = objN.insertCell(index++);
		objL.id = 'l'+node.id;
		objL.className = node.captionClass;
		objL.colSpan = 1 + node._myTree._levelMax - level;
		objL.onclick = captionEventHandler;
		objL.innerHTML = node.caption;
		
		this._update_on_context_menu(node, objL);
		if (must_clean_table) {
			this._clean_table(table, node._myTree);
		}
	}
}


/**
 * Add event listener on the label to call possibly context menu
 * @param {dNode} node The reference node
 * @param {object} objL The HTML element to modify
 * @private
 */
dFTreeDefaultLayout.prototype._update_on_context_menu= function(node, objL) {
	if (node.onContextMenu != null) {
		var fctn = "function(evt) { "+
				"evt = (evt) ? evt : ((event) ? event : null);"+
				"if (evt.preventDefault) {"+
					"evt.preventDefault();"+
				"}"+
				"evt.returnValue = false;"+
				node.onContextMenu+
			"}";
		if (!global_ie) {
			// W3C DOM event model
			eval("objL.addEventListener('contextmenu', "+fctn+", false);");
			// document.body.addEventListener("click", hideContextMenus, true);
		} else {
			// IE event model
			eval("objL.oncontextmenu = "+fctn+";");
		}
	}
}

/**
 * Refresh the HTML objects related to the given node
 * @param {dNode} node The reference node
 * @private
 */
dFTreeDefaultLayout.prototype._refresh= function(node) {
	var objN  = getObjectById("n"+node.id);
	var objP  = getObjectById("p"+node.id);
	var objL  = getObjectById("l"+node.id);
	var objI  = getObjectById("i"+node.id);

	if (objN != null)
	{
		var table = objN.parentNode.parentNode;
		var must_clean_table = false;

		//Handling open and close: checks node._io and changes class as needed
		if (!node._io) //just closed
		{
			// Apply changes on all childrens and sub-childrens
			var level = node._get_level();
			var interval = this._get_children_interval(node);
			rowIndex = interval.startIndex + interval.count - 1;
			for (i=0; i<interval.count; i++, rowIndex--) {
				var childRow = table.rows[rowIndex];
				if (childRow.style.display != "none") {
					childRow.style.display = "none";
					must_clean_table = true;
				}
			}
		} else {
			for (i in node._children) {
				var child  = getObjectById("n"+node._children[i].id);
				if (child) {
					if (child.style.display != "") {
						child.style.display = "";
						must_clean_table = true;
					}
				}
			}
		}
		
		if (must_clean_table) {
			this._clean_table(table, node._myTree);
		}

		objP.innerHTML = node._properPlus();
		objL.innerHTML = node.caption;
		if (objI != null) {
			if (node._myTree.useIcons)
				objI.innerHTML = node._properIcon();
			else
				objI.innerHTML = "";
		}

		var level = node._get_level();
		this._refresh_parent_vertical_lines(node, level, objN);
		this._update_on_context_menu(node, objL);
	}
}

/** 
 * Set the backgrounds of the first cells if the related ancestor is not the last child of its generation
 * @param {dNode} node The reference node
 * @private
 */
dFTreeDefaultLayout.prototype._refresh_parent_vertical_lines = function(node, level, row) {
	var parent = node._parent;
	var i = level;
	while (i > 0) {
		i--;
		if (parent != null) {
			this._update_vertical_lines(parent, row.cells[i]);
			parent = parent._parent;
		}
	}
}

/**
 * Set the background of the given object objV
 * No vertical line is displayed if the given node is the last child of its generation
 * @param {dNode} node The reference node
 * @param {obj} objV The HTML object to update
 * @private
 */
dFTreeDefaultLayout.prototype._update_vertical_lines = function(node, objV)
{
	// Update the vertical lines to connect the childrens
	var last = node._is_last();
	if (node._myTree.useIcons && !last) {
		objV.style.background = "transparent url("+node._myTree.icons.line+") repeat-y scroll left top";
	}
	else
	{
		objV.style.background = "";
	}
}

/**
 * Change attributes of the selected node
 * @param {dNode} node The reference node
 * @private
 */
dFTreeDefaultLayout.prototype._select_node = function(node)
{
	var objL;

	objL  = getObjectById("l"+node.id);

	//changes class to selected link
	if (objL)
	{
		objL.className = 'sl';
	}
}

/**
 * Restore the attributes of a previously selected node
 * @param {dNode} node The reference node
 * @private
 */
dFTreeDefaultLayout.prototype._unselect_node = function(node)
{
	var objL  = getObjectById("l"+node.id);

	//changes class to selected link
	if (objL)
	{
		objL.className = node.captionClass;
	}
}

/**
 * Remove the HTML objects related to the given node
 * @param {dNode} node The reference node
 * @private
 */
dFTreeDefaultLayout.prototype._erase_node = function(node)
{
	var objN = getObjectById("n"+node.id);
	if (objN)
	{
		var table = objN.parentNode.parentNode;
		table.deleteRow(objN.rowIndex);
	}
}

/**
 * Adjust the colSpan attribute taking into account the visible cells.
 * If colSpan is overestimated regarding the visible cells, some browser
 * (namely IE) give some strange results
 * @param {obj} table The HTML table
 * @param {dFTree} tree The reference tree
 * @private
 */
dFTreeDefaultLayout.prototype._clean_table = function(table, tree) {
	// Determine the visible column number
	var cols = 0;
	for (i=0; i<table.rows.length; i++) {
		var row = table.rows[i];
		if (row.style.display != "none") {
			if (cols < row.cells.length) cols = row.cells.length;
		}
	}
	
	// Determine the level
	var level = cols - 3;
	
	// if (level != tree._levelMax) {
		tree._levelMax = level;

		// Adjust colspan
		for (i=0; i<table.rows.length; i++) {
			var row = table.rows[i];
			if (row.style.display != "none") {
				var lastIndex = row.cells.length-1;
				var cell = row.cells[lastIndex];
				cell.colSpan = cols - lastIndex;
			}
		}
	// }
}

/**
 * dNode constructor 
 * Usage: a = new dNode({id:2, caption:'tree root', url:'http://www.w3.org'});
 * @constructor
 */
function dNode(arrayProps) {
	//mandatory fields
	this.id;          //node id
	this.caption;     //node caption

	//optional fields
	this.url;         //url to open
	this.target;      //target to open url
	this.onClick;     //javascript to execute onclick
	this.onOpen;      //javascript to execute when a node of the tree opens
	this.onClose;     //javascript to execute when a node of the tree closes
	this.onFirstOpen; //javascript to execute only on the first open
	this.onContextMenu; //javascript to execute on right click
	this.iconClosed;  //img.src of closed icon
	this.iconOpen;    //img.src of open icon
	this.runJS = true;       //(bool) if true, runs the onOpen/Close/FirstOpen props defined above
	this.plusSign = true;    //(bool) if the plus sign will appear or not
	this.isFolder = true;    //(bool) if the default icon is a folder
	this.captionClass = 'l'; //(string) the class for this node's caption


	//The parameters below are private
	this._opened = false; //already opened
	this._io = false; //is opened
	this._drawn = false; // true is already drawn
	this._undrawn_children = false;

	this._children = []; //references to children
	this._parent; //pointer to parent
	this._myTree; //pointer to myTree

	for (i in arrayProps)
	{
		if (i.charAt(0) != '_')
		{
			eval('this.'+i+' = arrayProps[\''+i+'\'];');
		}
	}
}

/**
 * Changes node state from open to closed, and vice-versa
 */
dNode.prototype.changeState = function()
{
	if (this._io)
	{
		this.close();
	}
	else
	{
		this.open();
	}

	if (this._myTree) this._myTree._setCookie();
}

/**
 * Changes node state from closed to open
 * If the node is already opened, the method call is just ignored
 */
dNode.prototype.open = function () {
	if (!this._io)
	{
		if (this._parent != null && this._myTree.useIcons && this._myTree.layout.closeBrothers)
		{
			this._myTree._closeBranch(this._parent._children);
		}
		this._io = true;
		if (!this._opened) 
		{
			if (this.runJS && this.onFirstOpen != null)
			{
				eval(this.onFirstOpen);
			}
			if (this._myTree.isLazy) {
				this._myTree._drawBranch(this._children);
			}
		}
		else if (this.runJS && this.onOpen != null)
		{
			eval(this.onOpen);
		}
		this._opened = true;
		if (this._undrawn_children) {
			this._undrawn_children = false;
			this._myTree._drawBranch(this._children);
		}
		this._refresh();
	}
}

/**
 * Changes state from close to open for the node and all its childrens
 */
dNode.prototype.expand = function () {
	this.open();
	for(i in this._children) {
		if (this._children[i] != null) this._children[i].expand();
	}
}

/**
 * Changes node state from open to closed
 * If the node is already closed, the method call is just ignored
 */
dNode.prototype.close = function() {
	if (this._io)
	{
		this._myTree._closeBranch(this._children);
		if (this.runJS && this.onClose != null)
		{
			eval(this.onClose);
		}
		this._io = false;
		this._refresh();
	}
}

/**
 * Synonym of close
 */
dNode.prototype.collapse = function() {
	this.close();
}

/**
 * Alter node label and other properties
 */
dNode.prototype.alter = function(arrayProps)
{
	for (i in arrayProps)
	{
		if (i != 'id' && i.charAt(0) != '_')
		{
			eval('this.'+i+' = arrayProps[\''+i+'\'];');
		}
	}
	//this._refresh();
	for(i in this._myTree._aNodes) {
		if (this._myTree._aNodes[i] != null) this._myTree._aNodes[i]._refresh();
	}
}

/**
 * css and dhtml refresh part
 * @private
 */
dNode.prototype._refresh = function() {
	this._myTree.layout._refresh(this);
	/// \TODO alter onLoad, etc

}

/**
 * Test if this node is the last of children of its familly
 * @private
 */
dNode.prototype._is_last = function()
{
	var last = true;
	if (this._parent) {
		index = this._myTree._searchNodeIn(this.id, this._parent._children);
		last = (index == this._parent._children.length - 1);
	} else {
		index = this._myTree._searchNodeIn(this.id, this._myTree._roots);
		last = (index == this._myTree._roots.length - 1);
	}
	return last;
}

/**
 * Gets the proper plus for this moment
 * @private
 */
dNode.prototype._properPlus = function()
{
	var last = this._is_last();
	if (this._children.length == 0) 
	{
		myPlusSign = false;
	}
	else
	{
		myPlusSign = this.plusSign;
	}
	if (!this._io) 
	{
		if (this._myTree.useIcons)
		{
			if (!last)
			{
				return (myPlusSign)?imageHTML(this._myTree.icons.plus):imageHTML(this._myTree.icons.join);
			}
			else
			{
				return (myPlusSign)?imageHTML(this._myTree.icons.plusBottom):imageHTML(this._myTree.icons.joinBottom);
			}
		}
		else
		{
			return (myPlusSign)?"+":"";
		}
	}
	else 
	{
		if (this._myTree.useIcons)
		{
			if (!last)
			{
				return (myPlusSign)?imageHTML(this._myTree.icons.minus):imageHTML(this._myTree.icons.join);
			}
			else
			{
				return (myPlusSign)?imageHTML(this._myTree.icons.minusBottom):imageHTML(this._myTree.icons.joinBottom);
			}
		}
		else
		{
			return (myPlusSign)?"-":"";
		}
	}
}

/**
 * Gets the proper icon for this moment
 * @private
 */
dNode.prototype._properIcon = function()
{
	if (!this._myTree.useIcons)
	{
		return "";
	}
	if (!this._io) 
	{
		if (this._iconClosed)
		{
			return imageHTML(this._iconClosed);
		}
		else if (!this.isFolder)
		{
			return imageHTML(this._myTree.icons.page);
		}
		else
		{
			return imageHTML(this._myTree.icons.folder);
		}
	}
	else 
	{
		if (this._iconOpen)
		{
			return imageHTML(this._iconOpen);
		} 
		else if (!this.isFolder) 
		{
			return imageHTML(this._myTree.icons.page);
		}
		else
		{
			return imageHTML(this._myTree.icons.folderOpen);
		}
	}
}

/**
 * Changes node to selected style class. Perform further actions.
 * @private
 */
dNode.prototype._select = function()
{
	if (this._myTree._selected)
	{
		this._myTree._selected._unselect();
	}
	this._myTree._selected = this;

	this._myTree.layout._select_node(this);
}

/**
 * Changes node to unselected style class. Perform further actions.
 * @private
 */
dNode.prototype._unselect = function()
{
	this._myTree._lastSelected = this._myTree._selected;
	this._myTree._selected = null;

	this._myTree.layout._unselect_node(this);
}

/**
 * state can be open or closed
 * warning: if drawn node is not child or root, bugs will happen
 * @private
 */
dNode.prototype._draw = function()
{
	if (!this._myTree._drawn) return;
	if (this._drawn) return;
	this._drawn = true;

	this._myTree.layout._draw_node(this);
}

/**
 * Erase the related HTML elements
 * @private
 */
dNode.prototype._erase = function()
{
	this._myTree.layout._erase_node(this);
}

/**
 * Level of the node in the tree
 * @return The level of the node. 0 for the root node.
 * @private
 */
dNode.prototype._get_level = function()
{
	level = 0;
	var parent = this._parent;
	while (parent != null) {
		level++;
		parent = parent._parent;
	}
	return level;
}

/**
 * dFTree constructor 
 * Usage: t = new dFTree({name:t, caption:'tree root', url:'http://www.w3.org'});
 * @constructor
 */
function dFTree(arrayProps) {
	//mandatory fields
	this.name;      //the value of this must be the name of the object

	//optional fields
	this.is_dynamic = true;   //tree is dynamic, i.e. updated on the fly
	this.followCookies = true;//use previous state (o/c) of nodes
	this.useIcons = false;     //use icons or not
	this.isLazy = false;   //lazy creation: HTML objects are only created when the parent is opened
	if (!arrayProps.layout) {
		this.layout = new dFTreeDefaultLayout({});
	}

	//arrayProps[icondir]: Icons Directory
	iconPath = (arrayProps['icondir'] != null)? arrayProps['icondir'] : '';

	this.icons = {
		root        : iconPath+'/foldertree_base.gif',
		folder      : iconPath+'/foldertree_folder.gif',
		folderOpen  : iconPath+'/foldertree_folderopen.gif',
		node        : iconPath+'/foldertree_folder.gif',
		empty       : iconPath+'/foldertree_empty.gif',
		line        : iconPath+'/foldertree_line.gif',
		join        : iconPath+'/foldertree_join.gif',
		joinBottom  : iconPath+'/foldertree_joinbottom.gif',
		page        : iconPath+'/foldertree_page.gif',
		plus        : iconPath+'/foldertree_plus.gif',
		plusBottom  : iconPath+'/foldertree_plusbottom.gif',
		minus       : iconPath+'/foldertree_minus.gif',
		minusBottom : iconPath+'/foldertree_minusbottom.gif',
		nlPlus      : iconPath+'/foldertree_nolines_plus.gif',
		nlMinus     : iconPath+'/foldertree_nolines_minus.gif'
	};

	//private
	this._roots = []; //reference to root nodes
	this._aNodes = [];
	this._lastSelected; //The last selected node
	this._selected; //The actual selected node
	this._drawn = false;
	
	for (i in arrayProps)
	{
		if (i.charAt(0) != '_')
		{
			eval('this.'+i+' = arrayProps[\''+i+'\'];');
		}
	}

}

/**
 * Insert in the HTML document in the current position the root HTML elements
 */
dFTree.prototype.draw = function() {
	if (this._drawn) return;
	this._drawn = true;
	this.layout._draw_tree(this);

	if (this._roots.length > 0)
	{
		this._getCookie();
		this._drawBranch(this._roots);
	}

}

/**
 * Recursive function, draws children
 * @param {array} childrenArray List of children
 * @private
 */
dFTree.prototype._drawBranch = function(childrenArray) {
	var a=0;
	for (a;a<childrenArray.length;a++)
	{
		childrenArray[a]._draw();
		if (!this.isLazy || childrenArray[a]._io)
		{ 
			this._drawBranch(childrenArray[a]._children);
		}
	}
}

/**
 * Recursive function, close children
 * @param {array} childrenArray List of children
 * @private
 */
dFTree.prototype._closeBranch = function(childrenArray) {
	for (i in childrenArray)
	{
		if (childrenArray[i] != null) childrenArray[i].close();
	}
}

/**
 * Close all nodes
 */
dFTree.prototype.collapseAll = function() {
	for (i in this._roots)
	{
		if (this._roots[i] != null) this._roots[i].collapse();
	}
}

/**
 * Expand all nodes
 */
dFTree.prototype.expandAll = function() {
	for (i in this._roots)
	{
		if (this._roots[i] != null) this._roots[i].expand();
	}
}

/**
 * Add a node to a parent
 * @param {dNode} node Node to add
 * @param {string} pid ID of the parent; if the ID is not found, the node becomes the root
 */
dFTree.prototype.add = function(node,pid) {
	var auxPos;
	var addNode = false;
	if (typeof (auxPos = this._searchNode(node.id)) != "number")
	{
		// if parent exists, add node as its child
		if (typeof (auxPos = this._searchNode(pid)) == "number")
		{
			node._parent = this._aNodes[auxPos];
			this._aNodes[auxPos]._children[this._aNodes[auxPos]._children.length] = node;
			addNode = true;
		}
		else //if parent cannot be found, it is a tree root
		{
			this._roots[this._roots.length] = node;
			addNode = true;
		}
		if (addNode)
		{
			this._aNodes[this._aNodes.length] = node;
			node._myTree = this;
			if (this.is_dynamic) 
			{
				if (!this.isLazy || (node._parent && node._parent._io && node._parent._drawn)) node._draw();
				if (node._parent && node._parent._drawn)
				{
					node._parent._refresh();
					if (node._parent._children.length > 1) 
					{
						var n = node._parent._children[node._parent._children.length - 2];
						if (n._drawn) n._refresh();
					}
				}
			}
		}
	} 
}

/**
 * Remove a node
 * @param {dNode} node Node to remove
 */
dFTree.prototype.remove = function(node) {
	var childPos;
	if (typeof (childPos = this._searchNode(node.id)) != "number")
	{
		alert("Node '"+node.id+" not found in "+this.name);
	}
	else
	{
		// Remove the children
		for (i=node._children.length - 1; i>=0; i--) {
			if (typeof(node._children[i]) == "object") {
				this.remove(node._children[i]);
			}
		}
		
		node._erase();
		
		var parent = node._parent;
		var list = null;
		if (parent === null)
		{
			list = this._roots;
		}
		else // if (parent !== null)
		{
			list = parent._children;
		}
		if (list !== null) {
			var auxPos = this._searchNodeIn(node.id, list);
			if (typeof(auxPos) == "number") 
			{
				list.splice(auxPos, 1);
			}
		}
		node._parent = null;
		this._aNodes.splice(childPos, 1);

		if (parent !== null) 
		{
			parent._refresh();
			if (parent._children.length >= 1) parent._children[parent._children.length - 1]._refresh();
		}
	} 
}

/**
 * Modify the properties of a node
 * @param {array} arrayProps The same properties list of dNode. It must contain the property .ID to retrieve the node in the tree.
 */
dFTree.prototype.alter = function(arrayProps) {
	if (arrayProps['id'])
	{
		this.getNodeById(arrayProps['id']).alter(arrayProps);
	}
}

/**
 * Get a node by its identification
 * @return The found dNode object
 * @param {string} nodeid The node identification to retrieve
 */
dFTree.prototype.getNodeById = function(nodeid) {
	return this._aNodes[this._searchNode(nodeid)];
}


/**
 * Searches for a node in the node array. 
 * @return The position in the array 4it
 * @param {string} id The node identification to retrieve
 * @private
 */
dFTree.prototype._searchNode = function(id) {
	return this._searchNodeIn(id, this._aNodes);
}

/**
 * Searches for a node in given array, returning the position of the array 4it
 * @return The position in the array 4it
 * @param {string} id The node identification to retrieve
 * @private
 */
dFTree.prototype._searchNodeIn = function(id, list) {
	var a=0;
	for (a;a<list.length;a++)
	{
		if (list[a].id == id)
		{
			return a;
		}
	}
	return false;
}

/**
 * Save tree state in a cookie
 * @private
 */
dFTree.prototype._setCookie = function() {
	if (this.followCookies)
	{
		var state = "";
		first = true;
		var i;
		for (i in this._aNodes)
		{
			if (this._aNodes[i]._io) 
			{
				if (first) 
				{
					first = false;
				}
				else
				{
					state += ",";
				}
				state += this._aNodes[i].id;
			}
		}		
		setCookie("cons"+this.name, state);
	}
}

/**
 * Read the cookie content and update the tree accordingly
 * @private
 */
dFTree.prototype._getCookie = function() {
	if (this.followCookies)
	{
		var state = getCookie("cons"+this.name);
		var start = 0;
		while (start < state.length)
		{
			var pos = state.indexOf(",", start);
			if (pos <= 0) pos = state.length; 
			nodename = state.substring(start, pos);
			var node = this.getNodeById(nodename);
			if (node)
			{
				node._io = true;
			}
			start = pos + 1;
		}
	}
}

// === Auxiliar functions ==================================

/**
 * For multi-browser compatibility, get an HTML element by its ID
 * @param {string} name Identification of the HTML element
 */
function getObjectById(name)
{   
    if (document.getElementById)
    {
        return document.getElementById(name);
    }
    else if (document.all)
    {
        return document.all[name];
    }
    else if (document.layers)
    {
        return document.layers[name];
    }
    return false;
}

/**
 * [Cookie] Clears a cookie
 * @param {string} cookieName Name of the cookie
 */
function clearCookie(cookieName) {
	var now = new Date();
	var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
	this.setCookie(cookieName, 'cookieValue', yesterday);
	this.setCookie(cookieName, 'cookieValue', yesterday);
};

/**
 * [Cookie] Sets value in a cookie
 * @param {string} cookieName Name of the cookie
 * @param {string} value Value of the cookie
 * @param {date} expires Expiration date
 * @param {string} path
 * @param {string} domain
 * @param {boolean} secure
 */
function setCookie(cookieName, cookieValue, expires, path, domain, secure) {
	document.cookie =
		escape(cookieName) + '=' + escape(cookieValue)
		+ (expires ? '; expires=' + expires.toGMTString() : '')
		+ (path ? '; path=' + path : '')
		+ (domain ? '; domain=' + domain : '')
		+ (secure ? '; secure' : '');
};

/**
 * [Cookie] Gets a value from a cookie
 * @param {string} cookieName Name of the cookie
 */
function getCookie(cookieName) {
	var cookieValue = '';
	var posName = document.cookie.indexOf(escape(cookieName) + '=');
	if (posName != -1) {
		var posValue = posName + (escape(cookieName) + '=').length;
		var endPos = document.cookie.indexOf(';', posValue);
		if (endPos != -1)
		{
			cookieValue = unescape(document.cookie.substring(posValue, endPos));
		}
		else 
		{
			cookieValue = unescape(document.cookie.substring(posValue));
		}
	}
	return (cookieValue);
};

/**
 * HTML code for images
 * @param {string} src Relative or absolute path to the image 
 * @param {string} attributes Other HTML image attributes 
 */
function imageHTML(src,attributes) {
	if (attributes === null)
	{
		attributes = '';
	}
	return "<img "+attributes+" src=\""+src+"\">";
}

var global_ie = is_ie();
function is_ie() {
	var ua = window.navigator.userAgent.toLowerCase();
	if ((ua.indexOf('msie')) != -1) {
		return true;
	} else {
		return false;
	}
}
