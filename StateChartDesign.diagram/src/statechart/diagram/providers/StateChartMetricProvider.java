package statechart.diagram.providers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;

import statechart.Node;
import statechart.StatechartPackage;
import statechart.diagram.part.StateChartDiagramEditor;
import statechart.diagram.part.StateChartDiagramEditorPlugin;
import statechart.diagram.part.StateChartDiagramEditorUtil;
import statechart.diagram.part.StateChartVisualIDRegistry;

/**
 * @generated
 */
public class StateChartMetricProvider {

	/**
	 * @generated
	 */
	public static class MetricsAction extends Action {

		/**
		 * @generated
		 */
		private IWorkbenchPage page;

		/**
		 * @generated
		 */
		public MetricsAction(IWorkbenchPage page) {
			setText("Metrics");
			this.page = page;
		}

		/**
		 * @generated
		 */
		public void run() {
			IWorkbenchPart workbenchPart = page.getActivePart();
			IViewPart metricsView = null;
			try {
				metricsView = page
						.findView(statechart.diagram.providers.StateChartMetricProvider.ResultView.VIEW_ID);
				if (metricsView == null) {
					metricsView = page
							.showView(statechart.diagram.providers.StateChartMetricProvider.ResultView.VIEW_ID);
				} else {
					if (metricsView != null
							&& workbenchPart instanceof IDiagramWorkbenchPart) {
						final IDiagramWorkbenchPart part = (IDiagramWorkbenchPart) workbenchPart;
						((ResultView) metricsView).setInput(part);
					}
					page.activate(metricsView);
				}
			} catch (PartInitException e) {
				StateChartDiagramEditorPlugin.getInstance().logError(
						"Diagram metric view failure", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @generated
	 */
	static List calculateMetrics(IDiagramWorkbenchPart diagramPart) {
		final DiagramEditPart diagramEditPart = diagramPart
				.getDiagramEditPart();
		try {
			return (List) diagramPart.getDiagramEditPart().getEditingDomain()
					.runExclusive(new RunnableWithResult.Impl() {

						public void run() {
							Diagram diagram = diagramEditPart.getDiagramView();
							ArrayList<ElementMetrics> metrics = new ArrayList<ElementMetrics>(
									50);
							calculateSemanticElementMetrics(diagramEditPart,
									metrics);
							setResult(metrics);
						}
					});
		} catch (InterruptedException e) {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * NOTE: metrics are being collected for domain elements contained in the semantic element associated with diagram view, actual diagram content (elements present there) is not taken into account.
	 * @generated
	 */
	static void calculateSemanticElementMetrics(
			DiagramEditPart diagramEditPart, List<ElementMetrics> metricsList) {
		Diagram diagram = diagramEditPart.getDiagramView();
		EObject next = diagram.getElement();
		Iterator/*<EObject>*/it = next != null ? next.eAllContents()
				: Collections.EMPTY_LIST.iterator();
		HashMap<EObject, ElementMetrics> target2row = new HashMap<EObject, ElementMetrics>();
		while (next != null) {
			ArrayList<Metric> res = new ArrayList<Metric>(5);
			if (StatechartPackage.eINSTANCE.getNode().isInstance(next)) {
				res.add(new Metric("NOSN", calcNOSN((Node) next), new Double(
						0.0), new Double(1.0)));
				res.add(new Metric("NOEN", calcNOEN((Node) next), new Double(
						0.0), new Double(1.0)));
			}
			if (StatechartPackage.eINSTANCE.getNode().isInstance(next)) {
				res.add(new Metric("NOSN", calcNOSN((Node) next), new Double(
						0.0), new Double(1.0)));
				res.add(new Metric("NOEN", calcNOEN((Node) next), new Double(
						0.0), new Double(1.0)));
			}
			if (!res.isEmpty()) {
				ElementMetrics row = new ElementMetrics(next,
						formatElementName(next),
						(Metric[]) res.toArray(new Metric[res.size()]));
				metricsList.add(row);
				target2row.put(next, row);
			}
			next = it.hasNext() ? (EObject) it.next() : null;
		}
		if (!target2row.isEmpty()) { // list was modified, need to process only semantic metrics
			// bind semantic elements to notation
			StateChartDiagramEditorUtil.LazyElement2ViewMap element2ViewMap = new StateChartDiagramEditorUtil.LazyElement2ViewMap(
					diagram, target2row.keySet());
			for (Iterator it2 = target2row.entrySet().iterator(); it2.hasNext();) {
				Map.Entry entry = (Map.Entry) it2.next();
				EObject semanticElement = (EObject) entry.getKey();
				View targetView = StateChartDiagramEditorUtil.findView(
						diagramEditPart, semanticElement, element2ViewMap);
				ElementMetrics elementMetrics = (ElementMetrics) entry
						.getValue();
				elementMetrics.diagramElementID = targetView.eResource()
						.getURIFragment(targetView);
			}
		}
	}

	/**
	 * @generated
	 */
	private static String formatViewName(View viewTarget) {
		StringBuffer notationQNameBuf = new StringBuffer();
		notationQNameBuf.append(formatElementName(viewTarget));
		if (viewTarget.getElement() != null) {
			notationQNameBuf
					.append("->").append(formatElementName(viewTarget.getElement())); //$NON-NLS-1$
		}
		int visualID = StateChartVisualIDRegistry.getVisualID(viewTarget);
		notationQNameBuf
				.append('[')
				.append(visualID < 0 ? Integer.toString(System
						.identityHashCode(viewTarget)) : Integer
						.toString(visualID)).append(']');
		return notationQNameBuf.toString();
	}

	/**
	 * @generated
	 */
	private static String formatElementName(EObject object) {
		return EMFCoreUtil.getQualifiedName(object, true);
	}

	/**
	 * @generated
	 */
	private static class ElementMetrics {

		/**
		 * @generated
		 */
		final Metric[] metrics;

		/**
		 * @generated
		 */
		final String targetElementQName;

		/**
		 * @generated
		 */
		final Image elementImage;

		/**
		 * @generated
		 */
		String diagramElementID;

		/**
		 * @generated
		 */
		ElementMetrics(EObject target, String name, Metric[] metrics) {
			assert metrics.length > 0;
			assert name != null;
			this.metrics = metrics;
			this.targetElementQName = name;
			EClass imageTarget = target.eClass();
			if (target instanceof View) {
				View viewTarget = (View) target;
				if ("".equals(viewTarget.getType()) && viewTarget.getElement() != null) { //$NON-NLS-1$
					imageTarget = viewTarget.getElement().eClass();
				}
			}
			this.elementImage = StateChartElementTypes.getImage(imageTarget);
		}

		/**
		 * @generated
		 */
		Metric getMetricByKey(String key) {
			for (int i = 0; i < metrics.length; i++) {
				if (metrics[i].key.equals(key)) {
					return metrics[i];
				}
			}
			return null;
		}
	}

	/**
	 * @generated
	 */
	private static class Metric implements Comparable {

		/**
		 * @generated
		 */
		final String key;

		/**
		 * @generated
		 */
		final Double value;

		/**
		 * @generated
		 */
		final Double lowLimit;

		/**
		 * @generated
		 */
		final Double highLimit;

		/**
		 * @generated
		 */
		final String displayValue;

		/**
		 * @generated
		 */
		Metric(String key, Double value) {
			this(key, value, null, null);
		}

		/**
		 * @generated
		 */
		Metric(String key, Double value, Double low, Double high) {
			assert key != null;
			this.key = key;
			this.value = value;
			this.lowLimit = low;
			this.highLimit = high;
			this.displayValue = (value != null) ? NumberFormat.getInstance()
					.format(value) : "null"; //$NON-NLS-1$
		}

		/**
		 * @generated
		 */
		public int compareTo(Object other) {
			Metric otherMetric = (Metric) other;
			if (value != null && otherMetric.value != null) {
				return (value.longValue() < otherMetric.value.longValue()) ? -1
						: (value.longValue() == otherMetric.value.longValue() ? 0
								: 1);
			}
			return (value == null && otherMetric.value == null) ? 0
					: (value == null) ? -1 : 1;
		}
	}

	/**
	 * @generated
	 */
	private static String[] getMetricKeys() {
		return new String[] { "NOSN", "NOEN" };
	}

	/**
	 * @generated
	 */
	private static String[] getMetricToolTips() {
		return new String[] {
				"startNodesNumber" + '\n' + "Number of Start States per level"
						+ '\n' + "low: 0.0" + "high: 1.0",
				"endNodesNumber" + '\n' + "Number of End States per level"
						+ '\n' + "low: 0.0" + "high: 1.0" };
	}

	/**
	 * @generated NOT
	 * 
	 */
	public static Double calcNOSN(Node target) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		double count  = 0.0;
		
		if(target.getType().equals("OR")){
			for(int i=0; i<target.getChildren().size(); i++){
				if(target.getChildren().get(i).getType().equals("START"))
					count = count + 1.0;
			}
			return count;
		}else
			return 0.0;
	}

	/**
	 * @generated NOT
	 */
	public static Double calcNOEN(Node target) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		double count  = 0.0;
		
		if(target.getType().equals("OR")){
			for(int i=0; i<target.getChildren().size(); i++){
				if(target.getChildren().get(i).getType().equals("END"))
					count = count + 1.0;
			}
			return count;
		}else
			return 0.0;
	}

	/**
	 * @generated
	 */
	public static class ResultView extends ViewPart implements IOpenListener {
		/**
		 * @generated
		 */
		public static final String VIEW_ID = "statechart.diagram.metricView"; //$NON-NLS-1$

		/**
		 * @generated
		 */
		private static int MAX_VISIBLE_KEY_CHAR_COUNT = 8;

		/**
		 * @generated
		 */
		private TableViewer viewer;

		/**
		 * @generated
		 */
		private Resource diagramResource;

		/**
		 * @generated
		 */
		void setInput(IDiagramWorkbenchPart diagramPart) {
			diagramResource = diagramPart.getDiagram().eResource();
			setTitleToolTip(diagramResource.getURI().path());
			List metrics = calculateMetrics(diagramPart);
			adjustLayout(metrics);
			viewer.setInput(metrics);
		}

		/**
		 * @generated
		 */
		private void adjustLayout(List metricResultList) {
			Map maxValStrMap = calcMetricMaxValueStrLenMap(metricResultList);
			Table table = viewer.getTable();
			TableLayout layout = new TableLayout();
			org.eclipse.swt.graphics.GC gc = new org.eclipse.swt.graphics.GC(
					table);

			gc.setFont(JFaceResources.getDialogFont());
			int padding = gc.stringExtent("X").x * 2; //$NON-NLS-1$
			for (int i = 0; i < getMetricKeys().length; i++) {
				final String nextKey = getMetricKeys()[i];
				String valueStr = (String) maxValStrMap.get(nextKey);
				int minWidth = valueStr != null ? gc.stringExtent(valueStr).x
						+ padding : 20;
				layout.addColumnData(new ColumnPixelData(minWidth, true));
			}
			gc.dispose();

			layout.addColumnData(new ColumnWeightData(1, 50, true));
			viewer.getTable().setLayout(layout);
			viewer.getTable().layout(true, true);
		}

		/**
		 * @generated
		 */
		public void createPartControl(Composite parent) {
			this.viewer = new TableViewer(parent, SWT.FULL_SELECTION);
			final Table table = viewer.getTable();
			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			for (int i = 0; i < getMetricKeys().length; i++) {
				TableColumn column = new TableColumn(table, SWT.NONE);
				column.setAlignment(SWT.RIGHT);
				column.setMoveable(true);
				column.setText(getMetricKeys()[i]);
				column.setToolTipText(getMetricToolTips()[i]);
			}

			TableColumn objectColumn = new TableColumn(table, SWT.NONE);
			objectColumn.setText("Element");
			objectColumn.setToolTipText("Measurement element");

			viewer.setLabelProvider(new Labels());
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.addOpenListener(this);

			SelectionListener headerSelListener = new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					table.setSortColumn((TableColumn) e.getSource());
					table.setSortDirection((table.getSortDirection() != SWT.DOWN) ? SWT.DOWN
							: SWT.UP);
					viewer.refresh();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			};
			TableColumn[] columns = viewer.getTable().getColumns();
			for (int i = 0; i < columns.length; i++) {
				columns[i].addSelectionListener(headerSelListener);
			}

			viewer.setSorter(new ViewerSorter() {
				public int compare(Viewer viewer, Object e1, Object e2) {
					TableColumn c = table.getSortColumn();
					int result = 0;
					if (c != null) {
						Metric mc1 = ((ElementMetrics) e1).getMetricByKey(c
								.getText());
						Metric mc2 = ((ElementMetrics) e2).getMetricByKey(c
								.getText());
						result = (mc1 != null && mc2 != null) ? mc1
								.compareTo(mc2) : (mc1 == null ? -1 : 1);
					} else {
						result = ((ElementMetrics) e1).targetElementQName
								.compareTo(((ElementMetrics) e2).targetElementQName);
					}
					return table.getSortDirection() == SWT.DOWN ? result
							: -result;
				}
			});
			IEditorPart editor = getSite().getPage().getActiveEditor();
			if (editor != null
					&& editor.getClass().equals(StateChartDiagramEditor.class)) {
				setInput((StateChartDiagramEditor) editor);
			}
		}

		/**
		 * @generated
		 */
		public void open(OpenEvent event) {
			try {
				IEditorPart editorPart = getSite().getPage()
						.openEditor(
								new FileEditorInput(
										WorkspaceSynchronizer
												.getFile(diagramResource)),
								StateChartDiagramEditor.ID);
				if (editorPart == null) {
					return;
				}
				IDiagramWorkbenchPart diagramPart = (IDiagramWorkbenchPart) editorPart
						.getAdapter(IDiagramWorkbenchPart.class);
				ElementMetrics selection = (ElementMetrics) ((IStructuredSelection) event
						.getSelection()).getFirstElement();
				String viewID = selection.diagramElementID;
				if (viewID != null) {
					View targetView = (View) diagramPart.getDiagram()
							.eResource().getEObject(viewID);
					if (targetView != null) {
						EditPart targetEditPart = (EditPart) diagramPart
								.getDiagramGraphicalViewer()
								.getEditPartRegistry().get(targetView);
						if (targetEditPart != null) {
							StateChartDiagramEditorUtil
									.selectElementsInDiagram(
											diagramPart,
											Collections
													.singletonList(targetEditPart));
						}
					}
				}
			} catch (PartInitException e) {
				StateChartDiagramEditorPlugin.getInstance().logError(
						"Can't open diagram editor", e); //$NON-NLS-1$
			}
		}

		/**
		 * @generated
		 */
		private static Map calcMetricMaxValueStrLenMap(List allMetrics) {
			Map metric2MaxStrLen = new HashMap();
			for (int i = 0; i < getMetricKeys().length; i++) {
				String nextKey = getMetricKeys()[i];
				int trimPos = Math.min(nextKey.length(),
						MAX_VISIBLE_KEY_CHAR_COUNT);
				metric2MaxStrLen.put(nextKey, nextKey.substring(0, trimPos));
			}
			for (Iterator it = allMetrics.iterator(); it.hasNext();) {
				ElementMetrics elementMetrics = (ElementMetrics) it.next();
				for (int i = 0; i < elementMetrics.metrics.length; i++) {
					Metric metric = elementMetrics.metrics[i];
					String valueStr = (String) metric2MaxStrLen.get(metric.key);
					if (valueStr == null
							|| metric.displayValue.length() > valueStr.length()) {
						metric2MaxStrLen.put(metric.key, metric.displayValue);
					}
				}
			}
			return metric2MaxStrLen;
		}

		/**
		 * @generated
		 */
		public void setFocus() {
		}

		/**
		 * @generated
		 */
		private class Labels extends LabelProvider implements
				ITableLabelProvider, ITableColorProvider {

			/**
			 * @generated
			 */
			private boolean isElementColumn(int columnIndex) {
				return columnIndex >= getMetricKeys().length;
			}

			/**
			 * @generated
			 */
			public Image getColumnImage(Object element, int columnIndex) {
				return isElementColumn(columnIndex) ? ((ElementMetrics) element).elementImage
						: null;
			}

			/**
			 * @generated
			 */
			public String getColumnText(Object element, int columnIndex) {
				ElementMetrics elementMetrics = (ElementMetrics) element;
				if (columnIndex == getMetricKeys().length) {
					return elementMetrics.targetElementQName;
				}
				final String key = getMetricKeys()[columnIndex];
				Metric metric = elementMetrics.getMetricByKey(key);
				return (metric != null) ? metric.displayValue : "-"; //$NON-NLS-1$
			}

			/**
			 * @generated
			 */
			public Color getBackground(Object element, int columnIndex) {
				return null;
			}

			/**
			 * @generated
			 */
			public Color getForeground(Object element, int columnIndex) {
				if (isElementColumn(columnIndex)) {
					return null;
				}
				ElementMetrics columnElement = (ElementMetrics) element;
				final String key = getMetricKeys()[columnIndex];
				Metric metric = columnElement.getMetricByKey(key);
				if (metric != null && metric.value != null) {
					if (metric.highLimit != null
							&& metric.highLimit.longValue() < metric.value
									.longValue()) {
						return ColorConstants.red;
					} else if (metric.lowLimit != null
							&& metric.lowLimit.longValue() > metric.value
									.longValue()) {
						return ColorConstants.blue;
					}
				}
				return null;
			}
		}

	}

}
