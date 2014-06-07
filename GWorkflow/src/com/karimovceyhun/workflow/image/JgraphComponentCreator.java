package com.karimovceyhun.workflow.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




import com.karimovceyhun.workflow.data.Collector;
import com.karimovceyhun.workflow.data.Decision;
import com.karimovceyhun.workflow.data.Node;
import com.karimovceyhun.workflow.data.Process;
import com.karimovceyhun.workflow.data.Separator;
import com.karimovceyhun.workflow.data.Terminate;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;

public class JgraphComponentCreator 
{
	private Node startProcess;
	private mxGraph graph = new mxGraph();
	private Object parent = graph.getDefaultParent();
	private ArrayList<Node> nodes;
	private int width = 50;
	private int height = 0;
	private int lastHeight = 0;
	private ArrayList<String> nodeNames;
	private Integer decisionSide = 10;
	
	public Object createCollector(String name,int posX,int posY)
	{
		Object triangle = graph.insertVertex(parent, null, name, posY, posX, 30, 40, mxConstants.STYLE_SHAPE+"=" + JGraphShape.values()[13].mxShapeConstantValue + ";perimeter=0;" + JGraphStyle.values()[5].mxStyle);
		return triangle;
	}
	
	
	public Object createSeperator(String name,int posX,int posY)
	{
		Object triangle = graph.insertVertex(parent, null, name, posY, posX, 30, 40, mxConstants.STYLE_SHAPE+"=" + JGraphShape.values()[13].mxShapeConstantValue + ";perimeter=0;" + JGraphStyle.values()[6].mxStyle);
		return triangle;
	}
	
	public Object createProcess(String name,int posX,int posY)
	{
		Object triangle = graph.insertVertex(parent, null, name, posY, posX, 40, 30);
		return triangle;
	}
	
	public Object createDecision(String name,int posX,int posY)
	{
		Object triangle = graph.insertVertex(parent, null, name, posY, posX, 26, 26,mxConstants.STYLE_SHAPE+"=" + JGraphShape.values()[3].mxShapeConstantValue + ";");
		return triangle;
	}
	
	public Object createStartPoint()
	{
		Object triangle = graph.insertVertex(parent, null, "", width, height + 5, 26, 26,mxConstants.STYLE_SHAPE+"=" + JGraphShape.values()[1].mxShapeConstantValue + ";" + JGraphStyle.values()[7].mxStyle);
		setLastHeight(height + height/200);
		return triangle;
	}
	
	public Object createEndPoint(String name,int posX,int posY)
	{
		Object triangle = graph.insertVertex(parent, null, name, posY, posX, 26, 26,mxConstants.STYLE_SHAPE+"=" + JGraphShape.values()[1].mxShapeConstantValue + ";");
		setLastHeight(height + height/200);
		return triangle;
	}
	
	public Object createDot(double posX,double posY)
	{
		return graph.insertVertex(parent, null, "", posX, posY, 1, 1);
	}

	public  mxGraphComponent getMxGraphComponent()
	{
		//testNodes();
		graph.getModel().beginUpdate();
		try
		{
			Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();
			style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.TopToBottom);
			/*Object v1 = createStartPoint();
			Object v2 = createProcess("2", 300, 5);
			Object v3 = createProcess("3", 400, 250);
			Object v4 = createProcess("3", 1000, 250);
			graph.insertEdge(parent, null, "Edge", v2, v1);
			graph.insertEdge(parent, null, "Edge", v1, v3);
			*/
			nodeNames = new ArrayList<String>();
			nodeNames.clear();
			constructGraph(height,width,startProcess);
			nodeNames.clear();
			constructEdges(startProcess);
			Object startObj = createStartPoint();
			graph.insertEdge(parent,null,"",startObj, startProcess.getGraphObject());
		}
		finally
		{
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		return graphComponent;
	}
	
	
	
	
	public enum JGraphShape 
	{
		RECTANGLE(mxConstants.SHAPE_RECTANGLE),
		ELLIPSE(mxConstants.SHAPE_ELLIPSE),
		DOUBLE_ELLIPSE(mxConstants.SHAPE_DOUBLE_ELLIPSE),
		RHOMBUS(mxConstants.SHAPE_RHOMBUS),
		LINE(mxConstants.SHAPE_LINE),
		IMAGE(mxConstants.SHAPE_IMAGE),
		CURVE(mxConstants.SHAPE_CURVE),
		LABEL(mxConstants.SHAPE_LABEL),
		CILINDER(mxConstants.SHAPE_CYLINDER),
		SWIMLANE(mxConstants.SHAPE_SWIMLANE),
		CONNECTOR(mxConstants.SHAPE_CONNECTOR),
		ACTOR(mxConstants.SHAPE_ACTOR),
		CLOUD(mxConstants.SHAPE_CLOUD),
		TRIANGLE(mxConstants.SHAPE_TRIANGLE),
		HEXAGON(mxConstants.SHAPE_HEXAGON);

		public String mxShapeConstantValue;

		JGraphShape(String mxShapeConstantValue) {
			this.mxShapeConstantValue = mxShapeConstantValue;
		}
	}

	public enum JGraphStyle 
	{
		OPACITY(mxConstants.STYLE_OPACITY, 50.0),
		TEXT_OPACITY(mxConstants.STYLE_TEXT_OPACITY, 50.0),
		OVERFLOW_1(mxConstants.STYLE_OVERFLOW, "visible"),
		OVERFLOW_2(mxConstants.STYLE_OVERFLOW, "hidden"),
		OVERFLOW_3(mxConstants.STYLE_OVERFLOW, "fill"),
		ROTATION1(mxConstants.STYLE_ROTATION, 90),
		ROTATION2(mxConstants.STYLE_ROTATION, -90),
		FILLCOLOR(mxConstants.STYLE_FILLCOLOR, mxUtils.getHexColorString(Color.BLACK)),
		GRADIENTCOLOR(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.getHexColorString(Color.BLUE)),
		GRADIENT_DIRECTION(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_EAST, mxConstants.STYLE_GRADIENTCOLOR, mxUtils.getHexColorString(Color.YELLOW)),
		STROKECOLOR(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.GREEN)),
		STROKEWIDTH(mxConstants.STYLE_STROKEWIDTH, 5),
		ALIGN(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT),
		VERTICAL_ALIGN(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM),
		LABEL_POSITION(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_LEFT),
		VERTICAL_LABEL_POSITION(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM),
		GLASS(mxConstants.STYLE_GLASS, 1),
		NOLABEL(mxConstants.STYLE_NOLABEL, 1),
		LABEL_BACKGROUNDCOLOR(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.getHexColorString(Color.CYAN)),
		LABEL_BORDERCOLOR(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.getHexColorString(Color.PINK)),
		SHADOW(mxConstants.STYLE_SHADOW, true),
		DASHED(mxConstants.STYLE_DASHED, true),
		ROUNDED(mxConstants.STYLE_ROUNDED, true),
		HORIZONTAL(mxConstants.STYLE_HORIZONTAL, false),
		FONTCOLOR(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.ORANGE)),
		FONTFAMILY(mxConstants.STYLE_FONTFAMILY, "Times New Roman"),
		FONTSIZE(mxConstants.STYLE_FONTSIZE, 15),
		FONTSTYLE(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD),
		;

		public String mxStyle;

		JGraphStyle(Object... values) {
			mxStyle = "";
			for (int i = 0; i < values.length; i++) {
				if(i%2==0) {
					mxStyle += values[i] + "=";
				} else {
					mxStyle += values[i] + ";";
				}
			}
		}
	}
	
	
	
	
	public void constructGraph(int initHeight,int initWidht,Node initNode)
	{
		Node asa = new Node();
		
		for(Node n = initNode; n !=null && !nodeNames.contains(n.getName()) ; )
		{
			if(n instanceof Process)
			{
					initHeight += 100;
					n.setGraphObject(createProcess(n.getName(), initHeight, initWidht));
					nodeNames.add(n.getName());
					n = ((Process) n).getSucceedingProcess();		
			}
			else if(n instanceof Decision)
			{
					initHeight += 100;
					nodeNames.add(n.getName());
					n.setGraphObject(createDecision(n.getName(), initHeight, initWidht));
					
					if(nodeNames.contains(((Decision)n).getRejectedsucceedingProcess().getName() ) )
					{
						constructGraph(initHeight , initWidht, ((Decision)n).getAcceptedsucceedingProcess());
					}
					else if(nodeNames.contains(((Decision)n).getAcceptedsucceedingProcess().getName() ))
					{
						constructGraph(initHeight , initWidht, ((Decision)n).getRejectedsucceedingProcess());
					}
					else
					{
						constructGraph(initHeight , initWidht, ((Decision)n).getAcceptedsucceedingProcess());
						constructGraph(initHeight - 100 , initWidht + 100, ((Decision)n).getRejectedsucceedingProcess());
					}
					break;
			}
			else if (n instanceof Separator)
			{
				int tempWidht = 0;
				initHeight += 100;
				nodeNames.add(n.getName());
				n.setGraphObject(createSeperator(n.getName(), initHeight, initWidht));
				for(Node node : ((Separator)n).getSucceedingProcesses() )
				{
					constructGraph(initHeight , initWidht + tempWidht, node);
					tempWidht += 200;
				}
				break;
			}
			else if(n instanceof Collector)
			{
				initHeight += 350;
				nodeNames.add(n.getName());
				n.setGraphObject(createCollector(n.getName(), initHeight, initWidht));
				n = ((Collector)n).getSucceedingProcess();
			}
			else if(n instanceof Terminate)
			{
				initHeight += 100;
				n.setGraphObject(createEndPoint(n.getName(), initHeight, initWidht));
				nodeNames.add(n.getName());
				break;
			}
		}
	}
	
	public void constructEdges(Node initNode)
	{
		for(Node n = initNode; n!=null && !nodeNames.contains(n.getName());)
		{
			if(n instanceof Process)
			{
				if( ((Process)n).getSucceedingProcess() != null)
				{
					nodeNames.add(n.getName());
					Object obj1 = n.getGraphObject();
					Object obj2 = ((Process)n).getSucceedingProcess().getGraphObject();
					
					if( nodeNames.contains(((Process)n).getSucceedingProcess().getName())  && ! (((Process)n).getSucceedingProcess() instanceof Collector))
					{
						double minX = java.lang.Math.min( ((mxCell)obj1).getGeometry().getX() - 30  , ((mxCell)obj2).getGeometry().getX() - 30);
						Object dotObj1 = createDot( minX, ((mxCell)obj1).getGeometry().getY() + 13);
						Object dotObj2 = createDot( minX, ((mxCell)obj2).getGeometry().getY() + 13);

						graph.insertEdge(parent, null, "", obj1, dotObj1,"endArrow=none");
						graph.insertEdge(parent, null, "", dotObj1, dotObj2,"endArrow=none");
						graph.insertEdge(parent, null, "", dotObj2, obj2);
					}
					else
					{
						graph.insertEdge(parent, null, "", obj1, obj2);
					}
				}
					n = ((Process) n).getSucceedingProcess();		
			}
			else if(n instanceof Decision)
			{
				nodeNames.add(n.getName());
				if( ((Decision)n).getAcceptedsucceedingProcess() != null )
				{
					Object obj1 = n.getGraphObject();
					Object obj2 = ((Decision)n).getAcceptedsucceedingProcess().getGraphObject();
					if( nodeNames.contains(((Decision)n).getAcceptedsucceedingProcess().getName()) )
					{
						decisionSide +=3;
						Object dotObj1 = createDot( ((mxCell)obj1).getGeometry().getX() - decisionSide, ((mxCell)obj1).getGeometry().getY() + 13);
						Object dotObj2 = createDot( ((mxCell)obj2).getGeometry().getX() - decisionSide, ((mxCell)obj2).getGeometry().getY() + 13);
						graph.insertEdge(parent, null, "", obj1, dotObj1,"endArrow=none");
						graph.insertEdge(parent, null, "", dotObj1, dotObj2,"endArrow=none");
						graph.insertEdge(parent, null, "", dotObj2, obj2);
					}
					else
					{
						graph.insertEdge(parent, null, "", obj1, obj2);
					}
					//graph.insertEdge(parent, null, "", obj1, obj2);
					constructEdges(  ((Decision)n).getAcceptedsucceedingProcess()  );
				}
				if (((Decision)n).getRejectedsucceedingProcess() != null)
				{
					decisionSide +=3;
					Object obj1 = n.getGraphObject();
					Object obj2 = ((Decision)n).getRejectedsucceedingProcess().getGraphObject();
					if( nodeNames.contains(((Decision)n).getRejectedsucceedingProcess().getName()) )
					{
						Object dotObj1 = createDot( ((mxCell)obj1).getGeometry().getX() - decisionSide, ((mxCell)obj1).getGeometry().getY() + 13);
						Object dotObj2 = createDot( ((mxCell)obj2).getGeometry().getX() - decisionSide, ((mxCell)obj2).getGeometry().getY() + 13);
						graph.insertEdge(parent, null, "", obj1, dotObj1,"endArrow=none");
						graph.insertEdge(parent, null, "", dotObj1, dotObj2,"endArrow=none");
						graph.insertEdge(parent, null, "", dotObj2, obj2);
					}
					else
					{
						graph.insertEdge(parent, null, "", obj1, obj2);
					}
					constructEdges(  ((Decision)n).getRejectedsucceedingProcess()  );
				}
				break;
			}
			else if(n instanceof Separator)
			{
				nodeNames.add(n.getName());
				if(((Separator) n).getSucceedingProcesses() != null)
				{
					Object obj1 = ((Separator) n).getGraphObject();
					for(Node node : ((Separator)n).getSucceedingProcesses())
					{
						Object obj2 = node.getGraphObject();
						graph.insertEdge(parent, null, "", obj1, obj2);
						constructEdges(node);
					}
					break;
				}
				
			}
			else if(n instanceof Collector)
			{
				nodeNames.add(n.getName());
				if( ((Collector)n).getSucceedingProcess() != null )
				{
					Object obj1 = ((Collector) n).getGraphObject();
					Object obj2 = ((Collector) n).getSucceedingProcess().getGraphObject();
					graph.insertEdge(parent, null, "", obj1, obj2);
					n = ((Collector)n).getSucceedingProcess();
				}
			}
			else if(n instanceof Terminate)
			{
				break;
			}
		}
	}


	
	/*public void testNodes()
	{
		nodeNames = new ArrayList<String>();
		Process p1 = new Process();
		Process p2 = new Process();
		Decision d1 = new Decision();
		Separator s1 = new Separator();
		Process p3 = new Process();
		Process p4 = new Process();
		Process p5 = new Process();
		Process p6 = new Process();
		List<Node> ls = new ArrayList<Node>();
		Collector c = new Collector();
		Terminate t = new Terminate();
		
		p1.setName("p1");
		p2.setName("p2");
		d1.setName("d1");
		s1.setName("s1");
		p3.setName("p3");
		p4.setName("p4");
		p5.setName("p5");
		p6.setName("p6");
		c.setName("c");
		t.setName("t");
		
		
		ls.add(p3);
		ls.add(p4);
		ls.add(p5);
		p3.setNextNode(c);
		p4.setNextNode(c);
		p5.setNextNode(p6);
		p6.setNextNode(c);
		c.setNextNode(t);
		
		p1.setNextNode(d1);
		d1.setNextSuccess(p1);
		d1.setNextFail(s1);
		s1.setNextProcesses(ls);
		
	    startNode = new Start();
		((Start)startNode).setNextNode(p1);
	}

	*/
	
	
	public mxGraph getGraph() {
		return graph;
	}

	public void setGraph(mxGraph graph) {
		this.graph = graph;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLastHeight() {
		return lastHeight;
	}

	public void setLastHeight(int lastHeight) {
		this.lastHeight = lastHeight;
	}

	public ArrayList<String> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(ArrayList<String> nodeNames) {
		this.nodeNames = nodeNames;
	}

	public JgraphComponentCreator(Node startProcess)
	{
		this.startProcess = startProcess;
	}
	
	public Node getStartProcess() 
	{
		return startProcess;
	}

	public void setStartProcess(Node startProcess) 
	{
		this.startProcess = startProcess;
	}
	

}
