/**
 * @file   clipboard-quotes.cpp
 * @Author Frank (Frank@CrookedThumbSoftware.com)
 * @date   November 2012
 * @brief  Will read the default (or specified) quote file, select a random line, and copy it to the clipboard.
 * Copyright (c) 2012, Crooked Thumb Software, All rights reserved.
 *
 * Uses QFile to read the file, stores all lines in memory in an QStringList.
 * If memory is more of an issue than speed, we could read the file once to get the line count, then a second
 * time to get the random line.
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <Qt/qapplication.h>
#include <Qt/qstringlist.h>
#include <Qt/qfile.h>
#include <Qt/qtextstream.h>
#include <Qt/qclipboard.h>

int main(int argc, char *argv[])
{
	//Needed to access the system clipboard
	QApplication app(argc, argv);

	//Default file path
	QString filePath("../Quotes.txt");
	//If there was a command line argument, use the 1st one as a file name
	if (argc > 1)
	{
		filePath = argv[1];
		if (argc > 2)
		{
			fprintf (stderr, "Usage is clipboard-quotes <optional path to quote file>.\n");
			return -3;
		}
	}

	//Try to open the file
	QFile file (filePath);
	if (!file.open(QIODevice::ReadOnly | QIODevice::Text))
	{
		fprintf (stderr, "Quote file '%s' is not available.\n", static_cast<const char *>(filePath.toAscii ()));
		return -1;
	}

	//Read the file into the list of strings
	QStringList quoteList;
	QTextStream fileStream(&file);
	while (!fileStream.atEnd())
	{
		quoteList.append (fileStream.readLine().trimmed());
	}

	file.close();

	if (quoteList.isEmpty())
	{
		fprintf (stderr, "Quote file '%s' is empty.\n", static_cast<const char *>(filePath.toAscii ()));
		return -2;
	}

	//Pick a random quote
	srand (time (NULL));
	int index = rand() % quoteList.size();
	QString randomQuote = "\"" + quoteList.at (index) + "\"";

	//Copy it to the clipboard
	QClipboard *pClipboard = app.clipboard();
	if (NULL != pClipboard)
	{
		pClipboard->setText(randomQuote);
	}

	//Don't need to keep the application open while something happens, so no
	//app.exec()
	return 0;
}
