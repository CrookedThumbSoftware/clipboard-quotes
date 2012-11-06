/**
 * @file   ClipboardQuotes.java
 * @Author Frank (Frank@CrookedThumbSoftware.com)
 * @date   November 2012
 * @brief  Will read the default (or specified) quote file, select a random line, and copy it to the clipboard.
 * Copyright (c) 2012, Crooked Thumb Software, All rights reserved.
 *
 * Uses LineNumberReader to read the file, stores all lines in memory in an ArrayList.
 * If memory is more of an issue than speed, we could read the file once to get the line count, then a second
 * time to get the random line.
 */
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * The main and only class. The class will read the quote file and copy a random
 * line to the clipboard
 */
public final class ClipboardQuotes implements ClipboardOwner
{
    /**
     * The list of quotes, one per line, from the file
     */
    private ArrayList<String> m_quoteList = new ArrayList<String> ();

    /**
     * The starting main function
     * 
     * @param aArguments the command line arguments
     */
    public static void main (String... aArguments)
    {
        ClipboardQuotes quoteInstance = new ClipboardQuotes ();
        // The default quotes file, included in the source tree relative to the bin directory
        String quoteFile = "../Quotes.txt";
        // If there is a command line argument, use as quote file
        if (aArguments.length > 0)
        {
            // If there are more than 1 arguments
            if (aArguments.length > 1)
            {
                // Let the user know the correct usage
                System.err.println ("Usage is ClipboardQuotes <optional path to quote file>");
                System.exit (1);
            } else
            {
                // Use the specified file
                quoteFile = aArguments[0];
            }
        }

        if (!quoteInstance.readQuoteFile (quoteFile))
        {
            System.err.println ("Quote file '" + quoteFile + "' is not a valid quote file.");
            System.exit (1);
        }
        int line = quoteInstance.pickRandomQuote ();
        quoteInstance.setClipboardToLine (line);
    }

    /**
     * Empty implementation inherited from the ClipboardOwner interface.
     */
    public void lostOwnership (Clipboard aClipboard, Transferable aContents)
    {
        // do nothing
    }

    /**
     * Reads the provided quote file into the array
     * 
     * @param quoteFile the quote file to use
     */
    public boolean readQuoteFile (String quoteFile)
    {
        boolean retFileRead = false;
        LineNumberReader lineNumberReader = null;

        try
        {
            // Construct the LineNumberReader object
            lineNumberReader = new LineNumberReader (new FileReader (quoteFile));

            String line = null;
            // For each line in the quote file
            while ((line = lineNumberReader.readLine ()) != null)
            {
                // Stuff the line in the dynamically-sized list
                m_quoteList.add (line);
            }
            
            // Finished reading the file, we can return okay
            if (!m_quoteList.isEmpty ())
            {
                retFileRead = true;
            } else {
                System.err.println ("Quote file is empty.");
            }
        } catch (FileNotFoundException ex)
        {
            // This is an expected exception if the file isn't found
            System.err.println ("Quote file is not found.");
        } catch (IOException ex)
        {
            ex.printStackTrace ();
        } finally
        {
            // Close the file, if it's open
            try
            {
                // If it was correctly allocated, close it
                if (lineNumberReader != null)
                {
                    lineNumberReader.close ();
                }
            } catch (IOException ex)
            {
                ex.printStackTrace ();
            }
        }
        return retFileRead;
    }

    /**
     * Use the count of quotes to pick a random line
     */
    public int pickRandomQuote ()
    {
        Random rnd = new Random ();
        int line = rnd.nextInt (m_quoteList.size ());
        return line;
    }

    /**
     * Place the quote on the system clipboard
     * 
     * @param line the line number of the quote to use
     */
    public void setClipboardToLine (int line)
    {
        // Build a quote string selection with quote characters
        StringSelection stringSelection = new StringSelection ("\"" + m_quoteList.get (line) + "\"");
        Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        clipboard.setContents (stringSelection, this);
    }
    
    /**
     * Clears the array
     */
    public void clear ()
    {
        m_quoteList.clear ();
    }
}