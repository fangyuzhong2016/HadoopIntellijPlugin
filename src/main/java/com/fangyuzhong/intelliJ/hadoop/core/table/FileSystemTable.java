package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.fangyuzhong.intelliJ.hadoop.core.ProjectRef;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class FileSystemTable<T extends FileSystemTableModel>
        extends JTable
        implements Disposable
{
    private static final int MAX_COLUMN_WIDTH = 300;
    private static final int MIN_COLUMN_WIDTH = 10;
    public static final Color GRID_COLOR = new JBColor(new Color(15132390), Color.DARK_GRAY);
    protected FileSystemTableGutter tableGutter;
    private ProjectRef projectRef;
    private double scrollDistance;
    private JBScrollPane scrollPane;
    private java.util.Timer scrollTimer;
    private int rowVerticalPadding;
    private boolean disposed;

    public void setModel(@NotNull TableModel dataModel)
    {
        if (dataModel == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"dataModel", "com/dci/intellij/dbn/core/ui/table/FileSystemTable", "setModel"}));
        }
        FileSystemTableModel tableModel = (FileSystemTableModel) dataModel;
        Disposer.register(this, tableModel);
        super.setModel(dataModel);
    }

    public FileSystemTable(T tableModel, boolean showHeader)
    {
        this(null, tableModel, showHeader);
    }

    public FileSystemTable(Project project, T tableModel, boolean showHeader)
    {
        super(tableModel);
        this.projectRef = new ProjectRef(project);
        setGridColor(GRID_COLOR);
        Font font = getFont();
        setFont(font);
        setBackground(UIUtil.getTextFieldBackground());

        adjustRowHeight(1);

        JTableHeader tableHeader = getTableHeader();
        if (!showHeader)
        {
            tableHeader.setVisible(false);
            tableHeader.setPreferredSize(new Dimension(-1, 0));
        } else
        {
            tableHeader.addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent e)
                {
                    FileSystemTable.this.scrollPane = ((JBScrollPane) UIUtil.getParentOfType(JBScrollPane.class, FileSystemTable.this));
                    if (FileSystemTable.this.scrollPane != null)
                    {
                        FileSystemTable.this.calculateScrollDistance();
                        if ((FileSystemTable.this.scrollDistance != 0.0D) && (FileSystemTable.this.scrollTimer == null))
                        {
                            FileSystemTable.this.scrollTimer = new Timer();
                            FileSystemTable.this.scrollTimer.schedule(new FileSystemTable.ScrollTask(), 100L, 100L);
                        }
                    }
                }
            });
            tableHeader.addMouseListener(new MouseAdapter()
            {
                public void mouseReleased(MouseEvent e)
                {
                    if (FileSystemTable.this.scrollTimer != null)
                    {
                        FileSystemTable.this.scrollTimer.cancel();
                        FileSystemTable.this.scrollTimer.purge();
                        FileSystemTable.this.scrollTimer = null;
                    }
                }
            });
        }
        Disposer.register(this, tableModel);
    }

    protected void adjustRowHeight(int padding)
    {
        this.rowVerticalPadding = padding;
        adjustRowHeight();
    }

    protected void adjustRowHeight()
    {
        Font font = getFont();
        FontRenderContext fontRenderContext = getFontMetrics(getFont()).getFontRenderContext();
        LineMetrics lineMetrics = font.getLineMetrics("ABC", fontRenderContext);
        int fontHeight = Math.round(lineMetrics.getHeight());
        setRowHeight(fontHeight + this.rowVerticalPadding * 2);
    }

    @NotNull
    public T getModel()
    {
        return (T)super.getModel();
    }

    private double calculateScrollDistance()
    {
        if (this.scrollPane != null)
        {
            JViewport viewport = this.scrollPane.getViewport();
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo != null)
            {
                double mouseLocation = pointerInfo.getLocation().getX();
                double viewportLocation = viewport.getLocationOnScreen().getX();

                Point viewPosition = viewport.getViewPosition();
                double contentLocation = viewport.getView().getLocationOnScreen().getX();
                if ((contentLocation < viewportLocation) && (mouseLocation < viewportLocation + 20.0D))
                {
                    this.scrollDistance = (-Math.min(viewPosition.x, viewportLocation - mouseLocation));
                } else
                {
                    int viewportWidth = viewport.getWidth();
                    int contentWidth = viewport.getView().getWidth();
                    if ((contentLocation + contentWidth > viewportLocation + viewportWidth) && (mouseLocation > viewportLocation + viewportWidth - 20.0D))
                    {
                        this.scrollDistance = (mouseLocation - viewportLocation - viewportWidth);
                    } else
                    {
                        this.scrollDistance = 0.0D;
                    }
                }
            }
        }
        return this.scrollDistance;
    }

    public Project getProject()
    {
        return this.projectRef.get();
    }

    public Object getValueAtMouseLocation()
    {
        Point location = MouseInfo.getPointerInfo().getLocation();
        location.setLocation(location.getX() - getLocationOnScreen().getX(), location.getY() - getLocationOnScreen().getY());
        return getValueAtLocation(location);
    }

    public Object getValueAtLocation(Point point)
    {
        int columnIndex = columnAtPoint(point);
        int rowIndex = rowAtPoint(point);
        return (columnIndex > -1) && (rowIndex > -1) ? getModel().getValueAt(rowIndex, columnIndex) : null;
    }

    public void accommodateColumnsSize()
    {
        for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
        {
            accommodateColumnSize(columnIndex, getColumnWidthSpan());
        }
    }

    public int getColumnWidthSpan()
    {
        return 22;
    }

    public int convertColumnIndexToView(int modelColumnIndex)
    {
        return super.convertColumnIndexToView(modelColumnIndex);
    }

    public int convertColumnIndexToModel(int viewColumnIndex)
    {
        return super.convertColumnIndexToModel(viewColumnIndex);
    }

    public void accommodateColumnSize(int colIndex, int span)
    {
        TableColumn column = getColumnModel().getColumn(colIndex);
        int columnIndex = column.getModelIndex();
        int preferredWidth = 0;

        JTableHeader tableHeader = getTableHeader();
        if (tableHeader != null)
        {
            Object headerValue = column.getHeaderValue();
            TableCellRenderer headerCellRenderer = column.getHeaderRenderer();
            if (headerCellRenderer == null)
            {
                headerCellRenderer = tableHeader.getDefaultRenderer();
            }
            Component headerComponent = headerCellRenderer.getTableCellRendererComponent(this, headerValue, false, false, 0, columnIndex);
            if (headerComponent.getPreferredSize().width > preferredWidth)
            {
                preferredWidth = headerComponent.getPreferredSize().width;
            }
        }
        T model = getModel();
        for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++)
        {
            if (preferredWidth > 300)
            {
                break;
            }
            Object value = model.getValueAt(rowIndex, columnIndex);
            TableCellRenderer renderer = getCellRenderer(rowIndex, columnIndex);
            if (renderer != null)
            {
                Component component = renderer.getTableCellRendererComponent(this, value, false, false, rowIndex, columnIndex);
                if (component.getPreferredSize().width > preferredWidth)
                {
                    preferredWidth = component.getPreferredSize().width;
                }
            }
        }
        if (preferredWidth > 300)
        {
            preferredWidth = 300;
        }
        if (preferredWidth < 10)
        {
            preferredWidth = 10;
        }
        preferredWidth += span;
        if (column.getPreferredWidth() != preferredWidth)
        {
            column.setPreferredWidth(preferredWidth);
        }
    }

    public void selectCell(int rowIndex, int columnIndex)
    {
        if ((rowIndex > -1) && (columnIndex > -1) && (rowIndex < getRowCount()) && (columnIndex < getColumnCount()))
        {
            Rectangle cellRect = getCellRect(rowIndex, columnIndex, true);
            if (!getVisibleRect().contains(cellRect))
            {
                scrollRectToVisible(cellRect);
            }
            if ((getSelectedRowCount() != 1) || (getSelectedRow() != rowIndex))
            {
                setRowSelectionInterval(rowIndex, rowIndex);
            }
            if ((getSelectedColumnCount() != 1) || (getSelectedColumn() != columnIndex))
            {
                setColumnSelectionInterval(columnIndex, columnIndex);
            }
        }
    }

    private class ScrollTask
            extends TimerTask
    {
        private ScrollTask()
        {
        }

        public void run()
        {
            if ((FileSystemTable.this.scrollPane != null) && (FileSystemTable.this.scrollDistance != 0.0D))
            {
                new SimpleLaterInvocator()
                {
                    protected void execute()
                    {
                        JViewport viewport = FileSystemTable.this.scrollPane.getViewport();
                        Point viewPosition = viewport.getViewPosition();
                        viewport.setViewPosition(new Point((int) (viewPosition.x + FileSystemTable.this.scrollDistance), viewPosition.y));
                        FileSystemTable.this.calculateScrollDistance();
                    }
                }.start();
            }
        }
    }

    protected FileSystemTableGutter createTableGutter()
    {
        return null;
    }

    public final FileSystemTableGutter getTableGutter()
    {
        if (this.tableGutter == null)
        {
            this.tableGutter = createTableGutter();
            if (this.tableGutter != null)
            {
                Disposer.register(this, this.tableGutter);
            }
        }
        return this.tableGutter;
    }

    public final void initTableGutter()
    {
        FileSystemTableGutter tableGutter = getTableGutter();
        if (tableGutter != null)
        {
            JScrollPane scrollPane = (JScrollPane) UIUtil.getParentOfType(JScrollPane.class, this);
            if (scrollPane != null)
            {
                scrollPane.setRowHeaderView(tableGutter);
            }
        }
    }

    public void stopCellEditing()
    {
        if (isEditing())
        {
            getCellEditor().stopCellEditing();
        }
    }

    public void dispose()
    {
        if (!this.disposed)
        {
            this.disposed = true;
            GUIUtil.removeListeners(this);
            this.listenerList = new EventListenerList();
            this.columnModel = new DefaultTableColumnModel();
            this.selectionModel = new DefaultListSelectionModel();
            this.tableHeader = null;
        }
    }

    public boolean isDisposed()
    {
        return this.disposed;
    }
}
