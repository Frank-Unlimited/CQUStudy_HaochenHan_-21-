package spiaotools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentParDetector
{
  private String[] filterTerms = { "A", "An", "And", "Because", "But", "He", "How", "However", "It", "Nonetheless", "She", "So", "That", "The", "These", "Therefore", "They", "This", "Those", "What", "Where", "Which", "Why" };
  private ArrayList<String> filterTermList;
  Pattern initialPattern = Pattern.compile("[A-Z][.]");

  private int TITLE_MAX_LEN = 40;
  private String title_start = "<head>"; private String title_end = "</head>\n\n";
  private boolean TAG_TITLE = false;
  private String CUSTOM_ABBREV_LIST;

  public SentParDetector()
  {
    this.filterTermList = new ArrayList(this.filterTerms.length);
    for (int i = 0; i < this.filterTerms.length; i++) {
      this.filterTermList.add(this.filterTerms[i]);
    }

    this.CUSTOM_ABBREV_LIST = getCustomAcronymList();
  }

  public void markTitle(boolean b)
  {
    this.TAG_TITLE = b;
  }

  public String markupRawText(int flag, String input)
  {
    if (input.equals("")) {
      System.err.println("Warning: No Input Text");
      return null;
    }

    String title = new String();
    StringBuffer txbody = new StringBuffer();

    if (this.TAG_TITLE)
    {
      this.TITLE_MAX_LEN = 70;
      this.TITLE_MAX_LEN = 70;

      int index1 = input.indexOf("\n");
      title = input.substring(0, index1).trim();
      if (title.equals("")) {
        while (title.equals("")) {
          index1 = input.indexOf("\n", index1 + 1);
          title = input.substring(0, index1).trim();
        }
      }

      if (title.length() <= this.TITLE_MAX_LEN)
        input = input.substring(index1).trim();
      else {
        title = "";
      }
    }

    input = input.replace("$", "\\$");
    input = input.replace('\r', ' ') + " </p>";

    input = input + "\n\n";

    String str = input.replace('`', '\'');
    str = replacePattern(str, "(\\'\\')|[”]", "\"");

    String regToMatch = "([.?!:\\)\\\"\\'\\]\\“\\”])(\\s+)?(\\n(\\s+)?\\n)(\\n+)?";
    String regForSub = "$1 </p>$4 <p> ";
    str = "<p> " + replacePattern(str, regToMatch, regForSub);

    regToMatch = "([.?!]\\\"?\\'?\\)?\\]?)(\\s+)?(\\n?)\\s+([A-Z]|[0-9]|\\\"|\\'|\\()";
    regForSub = "$1 </s> ^ $4";
    str = replacePattern(str, regToMatch, regForSub);

    str = replacePattern(str, "<p>\\s+", "<p> ^ ");
    str = replacePattern(str, "</p>", "</s> </p>");
    str = replacePattern(str, "\\^\\s</s>\\s</p>", "</p>");

    str = replacePattern(str, "(\\^)\\s(\\d|\\d\\d)([.])\\s</s>\\s\\^", "$1 $2$3");
    str = replacePattern(str, "(<p>\\s+\\^)\\s+(\\d+|[A-Z])([.])\\s</s>\\s</p>\\s+<p>\\s+\\^", "$1 $2$3");
    str = replacePattern(str, "(</s>\\s+\\^)\\s+(\\d+|[A-Z])([.])\\s</s>\\s</p>\\s+<p>\\s+\\^", "$1 $2$3");

    str = replacePattern(str, "\\s([\"(]?)(Mr|Mrs|Dr|Prof|Ms|Sir|Sr|St|Mt)([.])\\s</s>\\s\\^", " $1$2$3");

    str = replacePattern(str, "\\s(Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)([.])\\s+</s>\\s+\\^\\s+([a-z])", " $1$2 $3");
    str = replacePattern(str, "\\s(Ltd|Mon|Tue|Wed|Thu|Thur|Fri|Sat|Sun)([.])\\s+</s>\\s+\\^\\s+([a-z])", " $1$2 $3");

    str = replacePattern(str, "\\s(max|min|Max|Min)([.])\\s+</s>\\s+\\^\\s+(\\d|[%$£])", " $1$2 $3");
    str = replacePattern(str, "\\s([(]?)(kg|ft|oz|gm)([.])\\s</s>\\s\\^", " $1$2$3");

    str = replacePattern(str, "\\s([(]?)(Fig|Ref|ref)([.])\\s</s>\\s\\^", " $1$2$3");

    str = replacePattern(str, "\\s([(]?)(Co|et\\sal|pp|vs|eg|e[.]g|i[.]e|usu|ad|ed|eds|yr|yrs|lb|Cap|Col|Gen|Lieut|Esg)([.])\\s</s>\\s\\^", " $1$2$3");

    str = replacePattern(str, "\\s(\\d+)(l|lb)([.])\\s</s>\\s\\^", " $1$2$3");

    if (this.CUSTOM_ABBREV_LIST != null) {
      str = replacePattern(str, this.CUSTOM_ABBREV_LIST, " $1$2");
    }

    str = replacePattern(str, "(\\])(\\s+)([A-Z])", "$1 </s> \\^ $3");
    str = replacePattern(str, "([.?!]\\s+</s>\\s+\\^\\s+)([1-9]|[A-Z])([.]\\s+)</s>\\s+\\^", "$1 $2$3");
    str = replacePattern(str, "(\\s+[A-Z][.])\\s+</s>\\s+\\^\\s+([a-z])", "$1 $2");

    str = clearInBracket(str);

    str = replacePattern(str, "([A-Z]\\S+\\s+[A-Z][.]\\s+)</s>\\s+\\^", "$1 ");

    str = checkAcronym(str);

    str = replacePattern(str, "\\s<p>\\s</p>", "");

    str = str.replace("\\$", "$");

    StringTokenizer st = new StringTokenizer(str, "^");
    int count = 0;
    txbody.append(st.nextToken());
    while (st.hasMoreTokens()) {
      count++; txbody.append("<s n=\"" + count + "\">" + st.nextToken());
    }

    if (title.equals("")) {
      if (flag == 1)
        return reformat1(txbody.toString());
      if (flag == 2)
        return reformat2(txbody.toString());
      if (flag == 3)
        return reformat3(txbody.toString());
      if (flag == 4)
        return reformat4(txbody.toString());
      if (flag == 5) {
        return reformat5(txbody.toString(), input);
      }
      return reformatInternal(txbody.toString());
    }

    if (flag == 1)
      return this.title_start + title + this.title_end + reformat1(txbody.toString());
    if (flag == 2)
      return title + "\n\n" + reformat2(txbody.toString());
    if (flag == 3)
      return "<p>\n<s>\n" + title + "\n</s>\n</p>" + reformat3(txbody.toString());
    if (flag == 4)
      return title + "^\n" + reformat4(txbody.toString());
    if (flag == 5) {
      return title + "^\n" + reformat5(txbody.toString(), input);
    }
    return title + "^\n" + reformatInternal(txbody.toString());
  }

  private String checkAcronym(String text)
  {
    String acroRegexp = "([A-Z][.]){2,}\\s+</s>\\s+\\^\\s+([A-Z]\\w+)[,]?";
    Pattern acroPatrn = Pattern.compile(acroRegexp);
    Matcher acroMatch = acroPatrn.matcher(text);

    StringBuffer acrosb = new StringBuffer();

    while (acroMatch.find()) {
      String checkedString = acroMatch.group(0);
      String CapIniWord = acroMatch.group(2);

      if (!this.filterTermList.contains(CapIniWord)) {
        checkedString = checkedString.replaceFirst("(([A-Z][.]){2,})([,]?)\\s+</s>\\s+\\^\\s+([A-Z]\\w+)([,]?)", "$1 $3$4");
        acroMatch.appendReplacement(acrosb, checkedString);
      }
    }
    acroMatch.appendTail(acrosb);

    return acrosb.toString();
  }

  private String reformat1(String s)
  {
    StringTokenizer st = new StringTokenizer(s, " \n\t\r\f");
    StringBuffer sb = new StringBuffer();
    int space_flag = 0;
    while (st.hasMoreTokens()) {
      String tk = st.nextToken();
      if ((tk.equals("</s>")) || (tk.equals("<p>")) || (tk.equals("</p>"))) {
        sb.append(tk + "\n");
      } else if (tk.equals("<s")) {
        space_flag = 1;
        sb.append(tk + " " + st.nextToken());
      }
      else if (space_flag == 0) {
        sb.append(" " + tk);
      } else {
        sb.append(tk);
        space_flag = 0;
      }
    }

    return sb.toString();
  }

  private String reformat2(String s)
  {
    StringTokenizer st = new StringTokenizer(s, " \n\t\r\f");
    StringBuffer sb = new StringBuffer();
    int space_flag = 0;
    while (st.hasMoreTokens()) {
      String tk = st.nextToken();
      if ((tk.equals("</s>")) || (tk.equals("</p>"))) {
        sb.append("\n");
      } else if (tk.equals("<s")) {
        st.nextToken();
        space_flag = 1;
      } else {
        if (tk.equals("<p>")) {
          continue;
        }
        if (space_flag == 0) {
          sb.append(" " + tk);
        } else {
          sb.append(tk);
          space_flag = 0;
        }
      }
    }
    return sb.toString();
  }

  private String reformat3(String s) {
    StringTokenizer st = new StringTokenizer(s, " \n\t\r\f");
    StringBuffer sb = new StringBuffer();
    int space_flag = 0;
    while (st.hasMoreTokens()) {
      String tk = st.nextToken();
      if ((tk.equals("</s>")) || (tk.equals("<p>")) || (tk.equals("</p>"))) {
        sb.append("\n" + tk + "\n");
      } else if (tk.equals("<s")) {
        space_flag = 1;
        sb.append(tk + " " + st.nextToken() + "\n");
      }
      else {
        sb.append(tk + " ");
      }
    }
    return sb.toString();
  }

  private String reformatInternal(String s)
  {
    StringTokenizer st = new StringTokenizer(s, " \n\t\r\f");
    StringBuffer sb = new StringBuffer();
    int space_flag = 0;
    while (st.hasMoreTokens()) {
      String tk = st.nextToken();
      if ((tk.equals("</s>")) || (tk.equals("<p>")) || (tk.equals("</p>"))) {
        continue;
      }
      if (tk.equals("<s")) {
        space_flag = 1;
        st.nextToken();
        sb.append("^\n");
      }
      else if (space_flag == 0) {
        sb.append(" " + tk);
      } else {
        sb.append(tk);
        space_flag = 0;
      }
    }

    return sb.toString();
  }

  private String reformat4(String s)
  {
    StringTokenizer st = new StringTokenizer(s, " \n\t\r\f");
    StringBuffer sb = new StringBuffer();
    int space_flag = 0;
    while (st.hasMoreTokens()) {
      String tk = st.nextToken();

      if ((tk.equals("<p>")) || (tk.equals("</p>"))) {
        continue;
      }
      if (tk.equals("</s>")) {
        sb.append(tk + "\n");
      } else if (tk.equals("<s")) {
        space_flag = 1;
        sb.append(tk + " " + st.nextToken());
      }
      else if (space_flag == 0) {
        sb.append(" " + tk);
      } else {
        sb.append(tk);
        space_flag = 0;
      }
    }

    return sb.toString();
  }

  private String reformat5(String s, String origText)
  {
    StringTokenizer st = new StringTokenizer(s, " \n\t\r\f");
    StringBuffer sb = new StringBuffer();
    int space_flag = 0;

    ArrayList spOffsets = new ArrayList();
    int pCount = 0;
    int sCount = 0;
    int sStart = 0;
    int sEnd = 0;
    boolean recordSentStart = true;
    int startSerchPos = 0;
    int tkBegin = 0;
    int tkEnd = 0;

    while (st.hasMoreTokens())
    {
      String tk = st.nextToken();
      if (tk.equals("<p>")) {
        pCount++;
      }
      else if (tk.equals("<s")) {
        sCount++;
        recordSentStart = true;
      }
      else if (tk.equals("</s>")) {
        sEnd = tkEnd;

        int[] offsetRow = { pCount, sCount, sStart, sEnd };
        spOffsets.add(offsetRow);
      }
      else {
        if ((tk.equals("</p>")) || (tk.matches("n=\"\\d+\">")))
        {
          continue;
        }

        tkBegin = origText.indexOf(tk, startSerchPos);
        tkEnd = tkBegin + tk.length();

        if (recordSentStart) {
          sStart = tkBegin;
          recordSentStart = false;
        }

        startSerchPos += tk.length();
      }
    }

    Iterator itr = spOffsets.iterator();
    while (itr.hasNext()) {
      int[] passOffsets = (int[])itr.next();
      sb.append(passOffsets[0] + "\t" + passOffsets[1] + "\t" + passOffsets[2] + "\t" + passOffsets[3] + "\n");
    }

    return sb.toString();
  }

  private String replacePattern(String text, String ptrnStr, String replaceStr)
  {
    Pattern ptrn = Pattern.compile(ptrnStr);
    Matcher mtch = ptrn.matcher(text);
    return mtch.replaceAll(replaceStr);
  }

  private String clearInBracket(String txt)
  {
    StringBuffer sb = new StringBuffer();
    Pattern brackets = Pattern.compile("\\(.*?\\)", 32);

    Matcher matcher = brackets.matcher(txt);
    while (matcher.find()) {
      String matchString = matcher.group();

      Matcher iniMatcher = this.initialPattern.matcher(matchString);

      if (iniMatcher.find())
      {
        StringTokenizer stk = new StringTokenizer(matchString, " \t\n\b\r");

        if (stk.countTokens() < 35)
        {
          matchString = matchString.replaceAll("\\s+</s>\\s+\\^\\s+", " ");
          matcher.appendReplacement(sb, matchString);
        }
      }
    }

    matcher.appendTail(sb);
    return sb.toString();
  }

  private String getStrFromInstr(InputStream is)
  {
    try
    {
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line + "\n");
      }
      br.close();
      return sb.toString();
    }
    catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  private String getCustomAcronymList()
  {
    String abbrevListFilePath = "abbreviation_list.dat";
    try {
      File inf = new File(abbrevListFilePath);
      if (inf == null) {
        System.err.println("No customosed abbreviation list found.");
        return null;
      }

      InputStreamReader is = new InputStreamReader(new FileInputStream(inf), "UTF8");
      BufferedReader br = new BufferedReader(is);

      StringBuffer sb = new StringBuffer();
      sb.append("\\s([(]?)(");
      String line;
      while ((line = br.readLine()) != null) {
        if ((line.trim().equals("")) || (line.startsWith("/*")) || (line.startsWith("//")))
          continue;
        sb.append("|" + line);
      }
      br.close();
      sb.append(")\\s</s>\\s\\^");
      return sb.toString();
    }
    catch (IOException e)
    {
    }

    return null;
  }

  public static void main(String[] args)
  {
    SentParDetector spb = new SentParDetector();

    InputStream instr = System.in;
    if (instr == null) {
      System.out.println("Command-line usage: cat input_text | Java -jar sptoollit.jar");
      System.exit(1);
    }

    String out = spb.getStrFromInstr(instr);
    if (out == "") {
      System.out.println("Command-line usage: cat input_text | Java -jar sptoollit.jar");
      System.exit(1);
    }

    out = spb.markupRawText(1, out);

    System.out.println(out);

    out = null;
    System.exit(0);
  }
}

