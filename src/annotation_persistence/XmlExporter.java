package annotation_persistence;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 6, 2007
 * Time: 11:15:22 AM
 */

import java.lang.reflect.*;
import java.io.*;
import java.util.*;

/**
 * A very-simple persistent writer. Writes in XML format.
 * Cyclic object graphs are handled by ignoring the
 * cyclic field.  For example, consider a Company that has references
 * to its Employees, which have references to the Company.
 * Exporting the Company will export all the Employees, but
 * none of these Employees will contain a <Company>...</Company>
 * element. Exporting an Employee will cause the Company (and
 * all of it's employees except the original one) to be exported.
 * Note that this second situation is best avoided by not
 * marking the Employee's "Company" field as @Persistent.
 */
public class XmlExporter {
    private PrintWriter out;
    private int indentLevel = -1;
    private boolean writeClassNames = false;
    private Collection<Object> amVisiting = new ArrayList<Object>();

    /**
     * Create an XmlEporter.
     *
     * @param out             send the XML output to this writer.
     * @param writeClassNames if true, then synthesized element names
     *                        will have a "className=" attribute.
     */
    public XmlExporter(PrintWriter out, boolean writeClassNames) {
        this.out = out;
        this.writeClassNames = writeClassNames;
    }

    /**
     * Convenenience constructor, class names are written.
     * @param out print writer
     * @see #XmlExporter(PrintWriter,boolean)
     */
    public XmlExporter(PrintWriter out) {
        this(out, true);
    }

    public void flush(Object obj) throws IOException {
        flush(obj, null, null);
    }

    /**
     * Flush out a single object that's represented as a class (not
     * a primitive). The fields of the object are flushed
     * recursively. The output takes the form:
     * <PRE>
     * <eleName className="..." name="..." >
     * ...
     * </eleName>
     * <p/>
     * The element name typically defaults to the class name. However,
     * if the object is annotated and has a name= attribute (or
     * an unnamed value), then that attribute specifies the
     * element name. The className attribute is generated if
     * this XmlExporter was created with constructor's
     * writeClassNames argument set to true.
     * It holds the fully-qualified class name.
     *
     * @param obj                 The object to export. If the object is a Collection or
     *                            Map, then the elements are exported as if they had
     *                            been passed to flush(Object) one at a time.
     * @param nameAttribute       if not null, and the object is not annotated, then
     *                            use this string instead of the class name for the
     *                            element name.
     * @param fallbackElementName if not null, a name= attribute is
     *                            included in the generated element, and this
     *                            parameter determines the value.
     * @throws IOException  IOException
     */
    public void flush(Object obj, String nameAttribute, String fallbackElementName) throws IOException {
        ++indentLevel;

        if (amVisiting.contains(obj)) { // Cyclic object graph.
            return;                     // Silently ignore. cycles.
        }

        amVisiting.add(obj);

        if (obj instanceof Map) {
            for (Object element : ((Map) obj).keySet()) {
                flush(((Map) obj).get(element), element.toString(), null);
            }
        } else if (obj instanceof Collection) {
            for (Object o : ((Collection) obj)) {
                flush(o);
            }
        } else {
            Exportable annotation = obj.getClass().getAnnotation(Exportable.class);

            if (fallbackElementName == null) {
                fallbackElementName = extractNameFromClass(obj);
            }

            String elementName = fallbackElementName;

            if (annotation != null) {
                if (annotation.description().length() > 0) {
                    out.println("<!-- " + annotation.description() + " -->");
                }

                elementName = annotation.value();
                if (elementName.length() == 0) {    // If it's not specified in the value= attribute,
                    elementName = annotation.name();    //      check the name= attribute.
                }
                if (elementName.length() == 0) {    // It's not in the name= attribute either.
                    elementName = fallbackElementName;
                }
            }

            out.println(
                    indent()
                            + "<"
                            + elementName
                            + (!writeClassNames ? " " :
                            (" className=\"" + obj.getClass().getName() + "\" "))
                            + (nameAttribute == null ? " " :
                            (" name=\"" + nameAttribute + "\" "))
                            + ">");

            if (annotation != null) {   // If the object is exportable,
                flushFields(obj);     // then process its fields.
            } else {
                out.println(indent() + "\t" + obj.toString());
            }

            out.println(indent() + "</" + elementName + ">");
        }

        amVisiting.remove(obj);
        --indentLevel;
    }

    @SuppressWarnings("unchecked")
    private void flushFields(Object obj) throws IOException {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields) {
                Persistent annotation = f.getAnnotation(Persistent.class);
                if (annotation == null) {
                    continue;
                }

                f.setAccessible(true); // Make private fields accessible.

                String value = null;
                Class type = f.getType();
                if (type == byte.class) value = Byte.toString(f.getByte(obj));
                if (type == short.class) value = Short.toString(f.getShort(obj));
                if (type == char.class) value = Character.toString(f.getChar(obj));
                if (type == int.class) value = Integer.toString(f.getInt(obj));
                if (type == long.class) value = Long.toString(f.getLong(obj));
                if (type == float.class) value = Float.toString(f.getFloat(obj));
                if (type == double.class) value = Double.toString(f.getDouble(obj));
                if (type == String.class) value = (String) (f.get(obj));

                // If an element name is specified in the annotation, use it.
                // Otherwise, use the field name as the element name.
                String name = annotation.value();
                if (name.length() == 0) {
                    name = f.getName();
                }

                if (value != null)   // Then it's a primitive type or a String.
                {
                    out.println(indent() + "\t<" + name + ">");
                    out.println(indent() + "\t\t" + value);
                    out.println(indent() + "\t</" + name + ">");
                } else if (f.get(obj) instanceof Collection
                        || f.get(obj) instanceof Map) {
                    out.println(indent() + "\t<" + name + ">");
                    flush(f.get(obj), f.getName(), null);
                    out.println(indent() + "\t</" + name + ">");
                } else {
                    // The following if statement will kick out a type-safety warning
                    // from the compiler. Since we don't actually know the type (so
                    // can't cast it appropriately), there's no way to eliminate
                    // this warning.

                    if (type.getAnnotation(Exportable.class) != null) {
                        flush(f.get(obj), f.getName(), null);
                    } else {
                        flush(f.get(obj), null, name);
                    }
                }
            }
        }
        catch (IllegalAccessException e)   // Shouldn't happen
        {
            assert false : "Unexpected exception:\n" + e;
        }
    }

    /**
     * Get the class name from the prefix. If the fully-qualified name
     * contains a $, assume it's an inner class and the class name is
     * everything to the right of the rightmost $. Otherwise, if the
     * fully qualified name has a dot, then the class name is everything
     * to the right of the rightmost dot. Otherwise, the name is the
     * string returned from getClass().getName().
     *
     * @param obj class object
     * @return class name
     */
    private String extractNameFromClass(Object obj) {
        String name = obj.getClass().getName();
        int index;
        if ((index = name.lastIndexOf('$')) != -1)
            return name.substring(index + 1);

        if ((index = name.lastIndexOf('.')) != -1)
            return name.substring(index + 1);

        return name;
    }

    private static final String indents[] = new String[]
            {
                    /* 00 */    "",
                    /* 01 */    "\t",
                    /* 02 */    "\t\t",
                    /* 03 */    "\t\t\t",
                    /* 04 */    "\t\t\t\t",
                    /* 05 */    "\t\t\t\t\t",
                    /* 06 */    "\t\t\t\t\t\t",
                    /* 07 */    "\t\t\t\t\t\t\t",
                    /* 08 */    "\t\t\t\t\t\t\t\t",
                    /* 09 */    "\t\t\t\t\t\t\t\t\t",
                    /* 10 */    "\t\t\t\t\t\t\t\t\t\t",
                    /* 11 */    "\t\t\t\t\t\t\t\t\t\t\t",
                    /* 12 */    "\t\t\t\t\t\t\t\t\t\t\t\t",
                    /* 13 */    "\t\t\t\t\t\t\t\t\t\t\t\t\t",
                    /* 14 */    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
                    /* 15 */    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            };

    private String indent() {
        return indents[indentLevel % 16];
    }
}
