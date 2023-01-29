package br.com.calves.cookspringboot.cook;

/*
 * JBoss DNA (http://www.jboss.org/dna)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 *
 * JBoss DNA is free software. Unless otherwise indicated, all code in JBoss DNA
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * JBoss DNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * Implementado para O PT-BR por Alex Costa.
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Transforms words to singular, plural, humanized (human readable), underscore, camel case, or ordinal form. This is inspired by
 * the <a href="http://api.rubyonrails.org/classes/Inflector.html">Inflector</a> class in <a
 * href="http://www.rubyonrails.org">Ruby on Rails</a>, which is distributed under the <a
 * href="http://wiki.rubyonrails.org/rails/pages/License">Rails license</a>.
 *
 * @author Randall Hauch
 */
public class Inflector {

    public static final String PT_BR = "pt-br";

    public static final String EN = "en";

    protected void initialize(String lang) {
        if (PT_BR.equals(lang)) {
            //Pluralize
            this.addPluralize("$", "s");
            this.addPluralize("s$", "s");
            this.addPluralize("x$", "x");
            this.addPluralize("ao", "oes"); //ex. aviao
            this.addPluralize("([a|e|o|u])l$", "$1is"); //ex. papel
            this.addPluralize("(i)l$", "$1s"); //ex. fuzil
            this.addPluralize("([m|n])$", "ns"); // ex. viagem
            this.addPluralize("([r|s|z])$", "$1es"); // ex. amor

            //Singularize
            this.addSingularize("s$", "");
            this.addSingularize("is$", "il"); //ex. fuzil
            this.addSingularize("([a|e|o|u])is$", "$1l"); //ex. papeis
            this.addSingularize("(oes)$", "ao");
            this.addSingularize("(aes)$", "ao");
            this.addSingularize("(ns)$", "m"); // ex. viagens
            this.addSingularize("([r|z])es$", "$1"); //ex. amor

            /*
             * PADRAO PT-BR
             */

            this.addIrregular("abdomen", "abdomens");
            this.addIrregular("software", "softwares");
            this.addIrregular("alemao", "alemaes");
            this.addIrregular("artesa", "artesaos");
            this.addIrregular("bencao", "bencaos");
            this.addIrregular("cao", "caes");
            this.addIrregular("capelao", "capelaes");
            this.addIrregular("capitao", "capitaes");
            this.addIrregular("chao", "chaos");
            this.addIrregular("charlatao", "charlataes");
            this.addIrregular("cidadao", "cidadaos");
            this.addIrregular("consul", "consules");
            this.addIrregular("cristao", "cristaos");
            this.addIrregular("dificil", "dificeis");
            this.addIrregular("email", "emails");
            this.addIrregular("escrivao", "escrivaes");
            this.addIrregular("fossel", "fosseis");
            this.addIrregular("germen", "germens");
            this.addIrregular("grao", "graos");
            this.addIrregular("hifen", "hifens");
            this.addIrregular("irmao", "irmaos");
            this.addIrregular("liquen", "liquens");
            this.addIrregular("mal", "males");
            this.addIrregular("mao", "maos");
            this.addIrregular("orfao", "orfaos");
            this.addIrregular("pais", "paises");
            this.addIrregular("pai", "pais");
            this.addIrregular("pao", "paes");
            this.addIrregular("perfil", "perfis");
            this.addIrregular("projetil", "projeteis");
            this.addIrregular("reptil", "repteis");
            this.addIrregular("sacristao", "sacristaes");
            this.addIrregular("sotao", "sotaos");
            this.addIrregular("tabeliao", "tabeliaes");
            this.addIrregular("release", "releases");
            this.addIrregular("alocacao", "alocacoes");
            this.addUncountable("atlas", "lapis", "onibus", "pires", "species", "virus", "status", "ferias");
        } else {
            //Pluralize
            this.addPluralize("(ax|test)is$", "$1es");
            this.addPluralize("(octop|vir)us$", "$1i");
            this.addPluralize("(octop|vir)i$", "$1i"); // already plural
            this.addPluralize("(alias|status)$", "$1es");
            this.addPluralize("(bu)s$", "$1ses");
            this.addPluralize("(buffal|tomat)o$", "$1oes");
            this.addPluralize("([ti])um$", "$1a");
            this.addPluralize("([ti])a$", "$1a"); // already plural
            this.addPluralize("sis$", "ses");
            this.addPluralize("(?:([^f])fe|([lr])f)$", "$1$2ves");
            this.addPluralize("(hive)$", "$1s");
            this.addPluralize("([^aeiouy]|qu)y$", "$1ies");
            this.addPluralize("(x|ch|ss|sh)$", "$1es");
            this.addPluralize("(matr|vert|ind)ix|ex$", "$1ices");
            this.addPluralize("([m|l])ouse$", "$1ice");
            this.addPluralize("([m|l])ice$", "$1ice");
            this.addPluralize("^(ox)$", "$1en");
            this.addPluralize("(quiz)$", "$1zes");
            // Need to check for the following words that are already pluralized:
            this.addPluralize("(people|men|children|sexes|moves|stadiums)$", "$1"); // irregulars
            this.addPluralize("(oxen|octopi|viri|aliases|quizzes)$", "$1"); // special rules
            this.addPluralize("r$", "rs");
            this.addPluralize("e$", "es");
            this.addPluralize("n$", "ns");

            //Singularize
            this.addSingularize("(s|si|u)s$", "$1s"); // '-us' and '-ss' are already singular
            //Add
            this.addSingularize("s$", "");
            this.addSingularize("(n)ews$", "$1ews");
            this.addSingularize("([ti])a$", "$1um");
            this.addSingularize("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
            this.addSingularize("(^analy)ses$", "$1sis");
            this.addSingularize("(^analy)sis$", "$1sis"); // already singular, but ends in 's'
            this.addSingularize("([^f])ves$", "$1fe");
            this.addSingularize("(hive)s$", "$1");
            this.addSingularize("(tive)s$", "$1");
            this.addSingularize("([lr])ves$", "$1f");
            this.addSingularize("([^aeiouy]|qu)ies$", "$1y");
            this.addSingularize("(s)eries$", "$1eries");
            this.addSingularize("(m)ovies$", "$1ovie");
            this.addSingularize("(x|ch|ss|sh)es$", "$1");
            this.addSingularize("([m|l])ice$", "$1ouse");
            this.addSingularize("(bus)es$", "$1");
            this.addSingularize("(o)es$", "$1");
            this.addSingularize("(shoe)s$", "$1");
            this.addSingularize("(cris|ax|test)is$", "$1is"); // already singular, but ends in 's'
            this.addSingularize("(cris|ax|test)es$", "$1is");
            this.addSingularize("(octop|vir)i$", "$1us");
            this.addSingularize("(octop|vir)us$", "$1us"); // already singular, but ends in 's'
            this.addSingularize("(alias|status)es$", "$1");
            this.addSingularize("(alias|status)$", "$1"); // already singular, but ends in 's'
            this.addSingularize("^(ox)en", "$1");
            this.addSingularize("(vert|ind)ices$", "$1ex");
            this.addSingularize("(matr)ices$", "$1ix");
            this.addSingularize("(quiz)zes$", "$1");

            this.addIrregular("person", "people");
            this.addIrregular("woman", "women");
            this.addIrregular("man", "men");
            this.addIrregular("child", "children");
            this.addIrregular("sex", "sexes");
            this.addIrregular("stadium", "stadiums");
            this.addUncountable("equipment", "information", "rice", "money", "species", "series", "fish", "sheep");

        }
    }

    protected class Rule {

        protected final String expression;
        protected final Pattern expressionPattern;
        protected final String replacement;

        protected Rule( String expression,
                        String replacement ) {
            this.expression = expression;
            this.replacement = replacement != null ? replacement : "";
            this.expressionPattern = Pattern.compile(this.expression, Pattern.CASE_INSENSITIVE);
        }

        /**
         * Apply the rule against the input string, returning the modified string or null if the rule didn't apply (and no
         * modifications were made)
         *
         * @param input the input string
         * @return the modified string if this rule applied, or null if the input was not modified by this rule
         */
        protected String apply( String input ) {
            Matcher matcher = this.expressionPattern.matcher(input);
            if (!matcher.find()) return null;
            return matcher.replaceAll(this.replacement);
        }

        @Override
        public int hashCode() {
            return expression.hashCode();
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj != null && obj.getClass() == this.getClass()) {
                final Rule that = (Rule)obj;
                if (this.expression.equalsIgnoreCase(that.expression)) return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return expression + ", " + replacement;
        }
    }

    private LinkedList<Rule> plurals = new LinkedList<Rule>();
    private LinkedList<Rule> singulars = new LinkedList<Rule>();
    /**
     * The lowercase words that are to be excluded and not processed. This map can be modified by the users via
     * {@link #getUncountables()}.
     */
    private final Set<String> uncountables = new HashSet<String>();

    public Inflector(String lang) {
        initialize(lang);
    }

    protected Inflector( Inflector original ) {
        this.plurals.addAll(original.plurals);
        this.singulars.addAll(original.singulars);
        this.uncountables.addAll(original.uncountables);
    }

    @Override
    public Inflector clone() {
        return new Inflector(this);
    }

    // ------------------------------------------------------------------------------------------------
    // Usage functions
    // ------------------------------------------------------------------------------------------------

    /**
     * Returns the plural form of the word in the string.
     *
     * Examples:
     *
     * <pre>
     *   inflector.pluralize(&quot;post&quot;)               #=&gt; &quot;posts&quot;
     *   inflector.pluralize(&quot;octopus&quot;)            #=&gt; &quot;octopi&quot;
     *   inflector.pluralize(&quot;sheep&quot;)              #=&gt; &quot;sheep&quot;
     *   inflector.pluralize(&quot;words&quot;)              #=&gt; &quot;words&quot;
     *   inflector.pluralize(&quot;the blue mailman&quot;)   #=&gt; &quot;the blue mailmen&quot;
     *   inflector.pluralize(&quot;CamelOctopus&quot;)       #=&gt; &quot;CamelOctopi&quot;
     * </pre>
     *
     *
     *
     * Note that if the {@link Object#toString()} is called on the supplied object, so this method works for non-strings, too.
     *
     *
     * @param word the word that is to be pluralized.
     * @return the pluralized form of the word, or the word itself if it could not be pluralized
     * @see #singularize(Object)
     */
    public String pluralize( Object word ) {
        if (word == null) return null;
        String wordStr = word.toString().trim();
        if (wordStr.length() == 0) return wordStr;
        if (isUncountable(wordStr)) return wordStr;
        for (Rule rule : this.plurals) {
            String result = rule.apply(wordStr);
            if (result != null) return result;
        }
        return wordStr;
    }

    public String pluralize( Object word,
                             int count ) {
        if (word == null) return null;
        if (count == 1 || count == -1) {
            return word.toString();
        }
        return pluralize(word);
    }

    /**
     * Returns the singular form of the word in the string.
     *
     * Examples:
     *
     * <pre>
     *   inflector.singularize(&quot;posts&quot;)             #=&gt; &quot;post&quot;
     *   inflector.singularize(&quot;octopi&quot;)            #=&gt; &quot;octopus&quot;
     *   inflector.singularize(&quot;sheep&quot;)             #=&gt; &quot;sheep&quot;
     *   inflector.singularize(&quot;words&quot;)             #=&gt; &quot;word&quot;
     *   inflector.singularize(&quot;the blue mailmen&quot;)  #=&gt; &quot;the blue mailman&quot;
     *   inflector.singularize(&quot;CamelOctopi&quot;)       #=&gt; &quot;CamelOctopus&quot;
     * </pre>
     *
     *
     *
     * Note that if the {@link Object#toString()} is called on the supplied object, so this method works for non-strings, too.
     *
     *
     * @param word the word that is to be pluralized.
     * @return the pluralized form of the word, or the word itself if it could not be pluralized
     * @see #pluralize(Object)
     */
    public String singularize( Object word ) {
        if (word == null) return null;
        String wordStr = word.toString().trim();
        if (wordStr.length() == 0) return wordStr;
        if (isUncountable(wordStr)) return wordStr;
        for (Rule rule : this.singulars) {
            String result = rule.apply(wordStr);
            if (result != null) return result;
        }
        return wordStr;
    }

    /**
     * Converts strings to lowerCamelCase. This method will also use any extra delimiter characters to identify word boundaries.
     *
     * Examples:
     *
     * <pre>
     *   inflector.lowerCamelCase(&quot;active_record&quot;)       #=&gt; &quot;activeRecord&quot;
     *   inflector.lowerCamelCase(&quot;first_name&quot;)          #=&gt; &quot;firstName&quot;
     *   inflector.lowerCamelCase(&quot;name&quot;)                #=&gt; &quot;name&quot;
     *   inflector.lowerCamelCase(&quot;the-first_name&quot;,'-')  #=&gt; &quot;theFirstName&quot;
     * </pre>
     *
     *
     *
     * @param lowerCaseAndUnderscoredWord the word that is to be converted to camel case
     * @param delimiterChars optional characters that are used to delimit word boundaries
     * @return the lower camel case version of the word
     * @see #underscore(String, char[])
     * @see #camelCase(String, boolean, char[])
     * @see #upperCamelCase(String, char[])
     */
    public String lowerCamelCase( String lowerCaseAndUnderscoredWord,
                                  char... delimiterChars ) {
        return camelCase(lowerCaseAndUnderscoredWord, false, delimiterChars);
    }

    /**
     * Converts strings to UpperCamelCase. This method will also use any extra delimiter characters to identify word boundaries.
     *
     * Examples:
     *
     * <pre>
     *   inflector.upperCamelCase(&quot;active_record&quot;)       #=&gt; &quot;SctiveRecord&quot;
     *   inflector.upperCamelCase(&quot;first_name&quot;)          #=&gt; &quot;FirstName&quot;
     *   inflector.upperCamelCase(&quot;name&quot;)                #=&gt; &quot;Name&quot;
     *   inflector.lowerCamelCase(&quot;the-first_name&quot;,'-')  #=&gt; &quot;TheFirstName&quot;
     * </pre>
     *
     *
     *
     * @param lowerCaseAndUnderscoredWord the word that is to be converted to camel case
     * @param delimiterChars optional characters that are used to delimit word boundaries
     * @return the upper camel case version of the word
     * @see #underscore(String, char[])
     * @see #camelCase(String, boolean, char[])
     * @see #lowerCamelCase(String, char[])
     */
    public String upperCamelCase( String lowerCaseAndUnderscoredWord,
                                  char... delimiterChars ) {
        return camelCase(lowerCaseAndUnderscoredWord, true, delimiterChars);
    }

    /**
     * By default, this method converts strings to UpperCamelCase. If the <code>uppercaseFirstLetter</code> argument to false,
     * then this method produces lowerCamelCase. This method will also use any extra delimiter characters to identify word
     * boundaries.
     *
     * Examples:
     *
     * <pre>
     *   inflector.camelCase(&quot;active_record&quot;,false)    #=&gt; &quot;activeRecord&quot;
     *   inflector.camelCase(&quot;active_record&quot;,true)     #=&gt; &quot;ActiveRecord&quot;
     *   inflector.camelCase(&quot;first_name&quot;,false)       #=&gt; &quot;firstName&quot;
     *   inflector.camelCase(&quot;first_name&quot;,true)        #=&gt; &quot;FirstName&quot;
     *   inflector.camelCase(&quot;name&quot;,false)             #=&gt; &quot;name&quot;
     *   inflector.camelCase(&quot;name&quot;,true)              #=&gt; &quot;Name&quot;
     * </pre>
     *
     *
     *
     * @param lowerCaseAndUnderscoredWord the word that is to be converted to camel case
     * @param uppercaseFirstLetter true if the first character is to be uppercased, or false if the first character is to be
     *        lowercased
     * @param delimiterChars optional characters that are used to delimit word boundaries
     * @return the camel case version of the word
     * @see #underscore(String, char[])
     * @see #upperCamelCase(String, char[])
     * @see #lowerCamelCase(String, char[])
     */
    public String camelCase( String lowerCaseAndUnderscoredWord,
                             boolean uppercaseFirstLetter,
                             char... delimiterChars ) {
        if (lowerCaseAndUnderscoredWord == null) return null;
        lowerCaseAndUnderscoredWord = lowerCaseAndUnderscoredWord.trim();
        if (lowerCaseAndUnderscoredWord.length() == 0) return "";
        if (uppercaseFirstLetter) {
            String result = lowerCaseAndUnderscoredWord;
            // Replace any extra delimiters with underscores (before the underscores are converted in the next step)...
            if (delimiterChars != null) {
                for (char delimiterChar : delimiterChars) {
                    result = result.replace(delimiterChar, '_');
                }
            }

            // Change the case at the beginning at after each underscore ...
            return replaceAllWithUppercase(result, "(^|_)(.)", 2);
        }
        if (lowerCaseAndUnderscoredWord.length() < 2) return lowerCaseAndUnderscoredWord;
        return "" + Character.toLowerCase(lowerCaseAndUnderscoredWord.charAt(0))
               + camelCase(lowerCaseAndUnderscoredWord, true, delimiterChars).substring(1);
    }

    /**
     * Makes an underscored form from the expression in the string (the reverse of the {@link #camelCase(String, boolean, char[])
     * camelCase} method. Also changes any characters that match the supplied delimiters into underscore.
     *
     * Examples:
     *
     * <pre>
     *   inflector.underscore(&quot;activeRecord&quot;)     #=&gt; &quot;active_record&quot;
     *   inflector.underscore(&quot;ActiveRecord&quot;)     #=&gt; &quot;active_record&quot;
     *   inflector.underscore(&quot;firstName&quot;)        #=&gt; &quot;first_name&quot;
     *   inflector.underscore(&quot;FirstName&quot;)        #=&gt; &quot;first_name&quot;
     *   inflector.underscore(&quot;name&quot;)             #=&gt; &quot;name&quot;
     *   inflector.underscore(&quot;The.firstName&quot;)    #=&gt; &quot;the_first_name&quot;
     * </pre>
     *
     *
     *
     * @param camelCaseWord the camel-cased word that is to be converted;
     * @param delimiterChars optional characters that are used to delimit word boundaries (beyond capitalization)
     * @return a lower-cased version of the input, with separate words delimited by the underscore character.
     */
    public String underscore( String camelCaseWord,
                              char... delimiterChars ) {
        if (camelCaseWord == null) return null;
        String result = camelCaseWord.trim();
        if (result.length() == 0) return "";
        result = result.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2");
        result = result.replaceAll("([a-z\\d])([A-Z])", "$1_$2");
        result = result.replace('-', '_');
        if (delimiterChars != null) {
            for (char delimiterChar : delimiterChars) {
                result = result.replace(delimiterChar, '_');
            }
        }
        return result.toLowerCase();
    }

    /**
     * Returns a copy of the input with the first character converted to uppercase and the remainder to lowercase.
     *
     * @param words the word to be capitalized
     * @return the string with the first character capitalized and the remaining characters lowercased
     */
    public String capitalize( String words ) {
        if (words == null) return null;
        String result = words.trim();
        if (result.length() == 0) return "";
        if (result.length() == 1) return result.toUpperCase();
        return "" + Character.toUpperCase(result.charAt(0)) + result.substring(1).toLowerCase();
    }

    /**
     * Capitalizes the first word and turns underscores into spaces and strips trailing "_id" and any supplied removable tokens.
     * Like {@link #titleCase(String, String[])}, this is meant for creating pretty output.
     *
     * Examples:
     *
     * <pre>
     *   inflector.humanize(&quot;employee_salary&quot;)       #=&gt; &quot;Employee salary&quot;
     *   inflector.humanize(&quot;author_id&quot;)             #=&gt; &quot;Author&quot;
     * </pre>
     *
     *
     *
     * @param lowerCaseAndUnderscoredWords the input to be humanized
     * @param removableTokens optional array of tokens that are to be removed
     * @return the humanized string
     * @see #titleCase(String, String[])
     */
    public String humanize( String lowerCaseAndUnderscoredWords,
                            String... removableTokens ) {
        if (lowerCaseAndUnderscoredWords == null) return null;
        String result = lowerCaseAndUnderscoredWords.trim();
        if (result.length() == 0) return "";
        // Remove a trailing "_id" token
        result = result.replaceAll("_id$", "");
        // Remove all of the tokens that should be removed
        if (removableTokens != null) {
            for (String removableToken : removableTokens) {
                result = result.replaceAll(removableToken, "");
            }
        }
        result = result.replaceAll("_+", " "); // replace all adjacent underscores with a single space
        return capitalize(result);
    }

    /**
     * Capitalizes all the words and replaces some characters in the string to create a nicer looking title. Underscores are
     * changed to spaces, a trailing "_id" is removed, and any of the supplied tokens are removed. Like
     * {@link #humanize(String, String[])}, this is meant for creating pretty output.
     *
     * Examples:
     *
     * <pre>
     *   inflector.titleCase(&quot;man from the boondocks&quot;)       #=&gt; &quot;Man From The Boondocks&quot;
     *   inflector.titleCase(&quot;x-men: the last stand&quot;)        #=&gt; &quot;X Men: The Last Stand&quot;
     * </pre>
     *
     *
     *
     * @param words the input to be turned into title case
     * @param removableTokens optional array of tokens that are to be removed
     * @return the title-case version of the supplied words
     */
    public String titleCase( String words,
                             String... removableTokens ) {
        String result = humanize(words, removableTokens);
        result = replaceAllWithUppercase(result, "\\b([a-z])", 1); // change first char of each word to uppercase
        return result;
    }

    /**
     * Turns a non-negative number into an ordinal string used to denote the position in an ordered sequence, such as 1st, 2nd,
     * 3rd, 4th.
     *
     * @param number the non-negative number
     * @return the string with the number and ordinal suffix
     */
    public String ordinalize( int number ) {
        int remainder = number % 100;
        String numberStr = Integer.toString(number);
        if (11 <= number && number <= 13) return numberStr + "th";
        remainder = number % 10;
        if (remainder == 1) return numberStr + "st";
        if (remainder == 2) return numberStr + "nd";
        if (remainder == 3) return numberStr + "rd";
        return numberStr + "th";
    }

    // ------------------------------------------------------------------------------------------------
    // Management methods
    // ------------------------------------------------------------------------------------------------

    /**
     * Determine whether the supplied word is considered uncountable by the {@link #pluralize(Object) pluralize} and
     * {@link #singularize(Object) singularize} methods.
     *
     * @param word the word
     * @return true if the plural and singular forms of the word are the same
     */
    public boolean isUncountable( String word ) {
        if (word == null) return false;
        String trimmedLower = word.trim().toLowerCase();
        return this.uncountables.contains(trimmedLower);
    }

    /**
     * Get the set of words that are not processed by the Inflector. The resulting map is directly modifiable.
     *
     * @return the set of uncountable words
     */
    public Set<String> getUncountables() {
        return uncountables;
    }

    public void addPluralize( String rule,
                              String replacement ) {
        final Rule pluralizeRule = new Rule(rule, replacement);
        this.plurals.addFirst(pluralizeRule);
    }

    public void addSingularize( String rule,
                                String replacement ) {
        final Rule singularizeRule = new Rule(rule, replacement);
        this.singulars.addFirst(singularizeRule);
    }

    public void addIrregular( String singular,
                              String plural ) {
        //CheckArg.isNotEmpty(singular, "singular rule");
        //CheckArg.isNotEmpty(plural, "plural rule");
        String singularRemainder = singular.length() > 1 ? singular.substring(1) : "";
        String pluralRemainder = plural.length() > 1 ? plural.substring(1) : "";
        addPluralize("(" + singular.charAt(0) + ")" + singularRemainder + "$", "$1" + pluralRemainder);
        addSingularize("(" + plural.charAt(0) + ")" + pluralRemainder + "$", "$1" + singularRemainder);
    }

    public void addUncountable( String... words ) {
        if (words == null || words.length == 0) return;
        for (String word : words) {
            if (word != null) uncountables.add(word.trim().toLowerCase());
        }
    }

    /**
     * Utility method to replace all occurrences given by the specific backreference with its uppercased form, and remove all
     * other backreferences.
     *
     * The Java {@link Pattern regular expression processing} does not use the preprocessing directives <code>\l</code>,
     * <code>&#92;u</code>, <code>\L</code>, and <code>\U</code>. If so, such directives could be used in the replacement string
     * to uppercase or lowercase the backreferences. For example, <code>\L1</code> would lowercase the first backreference, and
     * <code>&#92;u3</code> would uppercase the 3rd backreference.
     *
     *
     * @param input
     * @param regex
     * @param groupNumberToUppercase
     * @return the input string with the appropriate characters converted to upper-case
     */
    protected static String replaceAllWithUppercase( String input,
                                                     String regex,
                                                     int groupNumberToUppercase ) {
        Pattern underscoreAndDotPattern = Pattern.compile(regex);
        Matcher matcher = underscoreAndDotPattern.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(groupNumberToUppercase).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Completely remove all rules within this inflector.
     */
    public void clear() {
        this.uncountables.clear();
        this.plurals.clear();
        this.singulars.clear();
    }

    public String normalize(String text) {
        String[] words = this.humanize(text).split(" ");
        StringBuilder normalizedText = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            normalizedText.append(this.humanize(words[i]));
        }
        return normalizedText.toString();
    }
}