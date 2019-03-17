/*
	This file is part of FreeJ2ME.

	FreeJ2ME is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	FreeJ2ME is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with FreeJ2ME.  If not, see http://www.gnu.org/licenses/
*/
package javax.microedition.lcdui;

import java.util.ArrayList;

import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformImage;
import org.recompile.mobile.PlatformGraphics;

public abstract class Displayable
{

	public PlatformImage platformImage;

	public int width = 0;

	public int height = 0;

	public boolean fullScreen = false;
	
	protected String title = "";

	protected ArrayList<Command> commands = new ArrayList<Command>();

	protected ArrayList<Item> items = new ArrayList<Item>();

	protected CommandListener commandlistener;

	protected boolean listCommands = false;
	
	protected int currentCommand = 0;

	protected int currentItem = -1;

	public Ticker ticker;

	public Displayable()
	{
		width = Mobile.getPlatform().lcdWidth;
		height = Mobile.getPlatform().lcdHeight;
	}

	public void addCommand(Command cmd)
	{ 
		try
		{
			commands.add(cmd);
		}
		catch (Exception e)
		{
			System.out.println("Problem Adding Command: "+e.getMessage());
		}
	}

	public void removeCommand(Command cmd) { commands.remove(cmd); }
	
	public int getWidth() { return width; }

	public int getHeight() { return height; }
	
	public String getTitle() { return title; }

	public void setTitle(String text) { title = text; }        

	public boolean isShown() { return true; }

	public Ticker getTicker() { return ticker; }

	public void setTicker(Ticker tick) { ticker = tick; }
	
	public void setCommandListener(CommandListener listener) { commandlistener = listener; }

	protected void sizeChanged(int width, int height) { }

	public Display getDisplay() { return Mobile.getDisplay(); }

	public ArrayList<Command> getCommands() { return commands; }


	public void keyPressed(int key) { }
	public void keyReleased(int key) { }
	public void pointerDragged(int x, int y) { }
	public void pointerPressed(int x, int y) { }
	public void pointerReleased(int x, int y) { }
	public void showNotify() { }
	public void hideNotify() { }

	public void notifySetCurrent() { }

	protected void render()
	{
		if(listCommands==true)
		{
			renderCommands();
		}
		else
		{
			renderItems();
		}
	}

	public void renderItems()
	{
		PlatformGraphics gc = platformImage.getGraphics();
		// Draw Background:
		gc.setColor(0xFFFFFF);
		gc.fillRect(0,0,width,height);
		gc.setColor(0x000000);
		
		// Draw Title:
		gc.drawString(title, width/2, 2, Graphics.HCENTER);
		gc.drawLine(0, 20, width, 20);
		gc.drawLine(0, height-20, width, height-20);

		if(items.size()>0)
		{
			if(currentItem<0) { currentItem = 0; }
			// Draw list items //
			int ah = height - 50; // allowed height
			int max = (int)Math.floor(ah / 15); // max items per page
			if(items.size()<max) { max = items.size(); }
		
			int page = 0;
			page = (int)Math.floor(currentItem/max); // current page
			int first = page * max; // first item to show
			int last = first + max - 1;

			if(last>=items.size()) { last = items.size()-1; }
			
			int y = 25;
			for(int i=first; i<=last; i++)
			{	
				if(currentItem == i)
				{
					gc.fillRect(0,y,width,15);
					gc.setColor(0xFFFFFF);
				}
				else {
	        gc.setColor(0x000000);
				}
				if(items.get(i) instanceof StringItem) {
				  __drawString((StringItem)items.get(i), gc, y, width, 15);
				}
				else if(items.get(i) instanceof ImageItem)
				{
          __drawImage((ImageItem)items.get(i), gc,y, width, 15);
				}
				else {
	        gc.drawString(items.get(i).getLabel(), width/2, y, Graphics.HCENTER);
				}
				y+=15;
			}
		}
		// Draw Commands
		switch(commands.size())
		{
			case 0: break;
			case 1:
				gc.drawString(commands.get(0).getLabel(), 3, height-17, Graphics.LEFT);
				gc.drawString(""+(currentItem+1)+" of "+items.size(), width-3, height-17, Graphics.RIGHT);
				break;
			case 2:
				gc.drawString(commands.get(0).getLabel(), 3, height-17, Graphics.LEFT);
				gc.drawString(commands.get(1).getLabel(), width-3, height-17, Graphics.RIGHT);
				break;
			default:
				gc.drawString("Options", 3, height-17, Graphics.LEFT);
		}

		if(this.getDisplay().getCurrent() == this)
		{
			Mobile.getPlatform().repaint(platformImage, 0, 0, width, height);
		}
	}

	protected void renderCommands()
	{
		PlatformGraphics gc = platformImage.getGraphics();

		// Draw Background:
		gc.setColor(0xFFFFFF);
		gc.fillRect(0,0,width,height);
		gc.setColor(0x000000);
		
		// Draw Title:
		gc.drawString("Options", width/2, 2, Graphics.HCENTER);
		gc.drawLine(0, 20, width, 20);
		gc.drawLine(0, height-20, width, height-20);

		if(commands.size()>0)
		{
			if(currentCommand<0) { currentCommand = 0; }
			// Draw commands //
			int ah = height - 50; // allowed height
			int max = (int)Math.floor(ah / 15); // max items per page			
			if(commands.size()<max) { max = commands.size(); }

			int page = 0;
			page = (int)Math.floor(currentCommand/max); // current page
			int first = page * max; // first item to show
			int last = first + max - 1;

			if(last>=commands.size()) { last = commands.size()-1; }
			
			int y = 25;
			for(int i=first; i<=last; i++)
			{	
				if(currentCommand == i)
				{
					gc.fillRect(0,y,width,15);
					gc.setColor(0xFFFFFF);
				}
				
				gc.drawString(commands.get(i).getLabel(), width/2, y, Graphics.HCENTER);
				
				gc.setColor(0x000000);
				y+=15;
			}
		}
		gc.drawString("Okay", 3, height-17, Graphics.LEFT);
		gc.drawString("Back", width-3, height-17, Graphics.RIGHT);

		if(this.getDisplay().getCurrent() == this)
		{
			Mobile.getPlatform().repaint(platformImage, 0, 0, width, height);
		}
	}

	protected void keyPressedCommands(int key)
	{
		switch(key)
		{
			case Mobile.KEY_NUM2: currentCommand--; break;
			case Mobile.KEY_NUM8: currentCommand++; break;
			case Mobile.NOKIA_UP: currentCommand--; break;
			case Mobile.NOKIA_DOWN: currentCommand++; break;
			case Mobile.NOKIA_SOFT1: doLeftCommand(); break;
			case Mobile.NOKIA_SOFT2: doRightCommand(); break;
			case Mobile.KEY_NUM5: doLeftCommand(); break;
		}
		if(currentCommand>=commands.size()) { currentCommand = 0; }
		if(currentCommand<0) { currentCommand = commands.size()-1; }
		if(listCommands==true) { renderCommands(); }
	}

	protected void doCommand(int index)
	{
		if(index>=0 && commands.size()>index)
		{
			if(commandlistener!=null)
			{
				commandlistener.commandAction(commands.get(index), this);
			}
		}
	}

	protected void doDefaultCommand()
	{
		doCommand(0);
	}

	protected void doLeftCommand()
	{
		if(commands.size()>2)
		{
			if(listCommands == true)
			{
				doCommand(currentCommand);
			}
			else
			{
				listCommands = true;
				currentCommand = 0;
				render();
			}
			return;
		}
		else
		{
			if(commands.size()>0 && commands.size()<=2)
			{
				doCommand(0);
			}
		}
	}

	protected void doRightCommand()
	{
		if(listCommands==true)
		{
			listCommands = false;
			currentCommand = 0;
			render();
		}
		else
		{
			if(commands.size()>0 && commands.size()<=2)
			{
				doCommand(1);
			}
		}
	}

  public static void __drawString(StringItem si, PlatformGraphics gc, int y, int sx, int sy) {
    gc.drawString(si.getLabel(), sx / 2, y, Graphics.HCENTER);
    gc.drawString(si.getText(), sx / 2, y, Graphics.HCENTER);
  }

  public static void __drawImage(ImageItem imageItem, PlatformGraphics gc, int y, int sx, int sy) {
    Image im = imageItem.getImage();
    int isw = im.getWidth();
    int isy = im.getHeight();
    int px1;
    int px2;
    int py1;
    int py2;
    int mode;
    py1 = y + ((isy < sy) ? (sy - isy) / 2 : 0);
    py2 = y;
    switch (imageItem.getLayout()) {
      case Item.LAYOUT_CENTER:
        px1 = sx / 2;
        px2 = sx / 2;
        mode = Graphics.HCENTER;
        break;
      case Item.LAYOUT_RIGHT:
        px1 = sx;
        px2 = sx - isw;
        mode = Graphics.RIGHT;
        break;
      default:
        px1 = 0;
        px2 = isw + 1;
        mode = Graphics.LEFT;
        break;
    }
    gc.drawString(imageItem.getLabel(), px2, py2, mode);
    gc.drawImage(imageItem.getImage(), px1, py1, mode);
  }

}
