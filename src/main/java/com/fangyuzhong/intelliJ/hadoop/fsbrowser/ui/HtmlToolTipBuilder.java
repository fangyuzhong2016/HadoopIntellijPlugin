package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public abstract class HtmlToolTipBuilder
        implements ToolTipProvider
{
    private StringBuilder buffer;

    public String getToolTip()
    {
        this.buffer = new StringBuilder();
        this.buffer.append("<html>");
        this.buffer.append("<table><tr><td><table cellpadding=0 cellspacing=0>\n");

        buildToolTip();

        closeOpenRow();
        this.buffer.append("</table></td></tr></table>");
        this.buffer.append("</html>");
        return this.buffer.toString();
    }

    public abstract void buildToolTip();

    public void append(boolean newRow, String text, boolean bold)
    {
        append(newRow, text, null, null, bold);
    }

    public void append(boolean newRow, String text, String size, String color, boolean bold)
    {
        if (newRow)
        {
            createNewRow();
        }
        if (bold)
        {
            this.buffer.append("<b>");
        }
        if ((color != null) || (size != null))
        {
            this.buffer.append("<font");
            if (color != null)
            {
                this.buffer.append(" color='").append(color).append("'");
            }
            if (size != null)
            {
                this.buffer.append(" size='").append(size).append("'");
            }
            this.buffer.append(">");
        }
        this.buffer.append(text);
        if ((color != null) || (size != null))
        {
            this.buffer.append("</font>");
        }
        if (bold)
        {
            this.buffer.append("</b>");
        }
    }

    public void createEmptyRow()
    {
        closeOpenRow();
        this.buffer.append("<tr><td>&nbsp;</td></tr>\n");
    }

    private void createNewRow()
    {
        closeOpenRow();
        this.buffer.append("<tr><td>");
    }

    private void closeOpenRow()
    {
        if (this.buffer.charAt(this.buffer.length() - 1) != '\n')
        {
            this.buffer.append("</td></tr>\n");
        }
    }
}
