import java.io.*;
import java.util.regex.*;

public class AnalizadorLexico {

    // Expresiones regulares para tokens
    private static final Pattern stringPattern = Pattern.compile("^\"(.*?)\"");
    private static final Pattern numberPattern = Pattern.compile("^[0-9]+(\\.[0-9]+)?((e|E)(\\+|-)?[0-9]+)?");
    private static final Pattern truePattern = Pattern.compile("^(true|TRUE)");
    private static final Pattern falsePattern = Pattern.compile("^(false|FALSE)");
    private static final Pattern nullPattern = Pattern.compile("^(null|NULL)");

    public static void main(String[] args) {
        try (
            BufferedReader br = new BufferedReader(new FileReader("fuente.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))
        ) {
            String linea;
            int numLinea = 1;
            while ((linea = br.readLine()) != null) {
                String original = linea;
                linea = linea.trim();
                StringBuilder resultado = new StringBuilder();
                while (!linea.isEmpty()) {
                    linea = linea.stripLeading();
                    if (linea.startsWith("{")) {
                        resultado.append("L_LLAVE ");
                        linea = linea.substring(1);
                    } else if (linea.startsWith("}")) {
                        resultado.append("R_LLAVE ");
                        linea = linea.substring(1);
                    } else if (linea.startsWith("[")) {
                        resultado.append("L_CORCHETE ");
                        linea = linea.substring(1);
                    } else if (linea.startsWith("]")) {
                        resultado.append("R_CORCHETE ");
                        linea = linea.substring(1);
                    } else if (linea.startsWith(",")) {
                        resultado.append("COMA ");
                        linea = linea.substring(1);
                    } else if (linea.startsWith(":")) {
                        resultado.append("DOS_PUNTOS ");
                        linea = linea.substring(1);
                    } else {
                        Matcher m;
                        m = stringPattern.matcher(linea);
                        if (m.find()) {
                            resultado.append("STRING ");
                            linea = linea.substring(m.end());
                            continue;
                        }
                        m = numberPattern.matcher(linea);
                        if (m.find()) {
                            resultado.append("NUMBER ");
                            linea = linea.substring(m.end());
                            continue;
                        }
                        m = truePattern.matcher(linea);
                        if (m.find()) {
                            resultado.append("PR_TRUE ");
                            linea = linea.substring(m.end());
                            continue;
                        }
                        m = falsePattern.matcher(linea);
                        if (m.find()) {
                            resultado.append("PR_FALSE ");
                            linea = linea.substring(m.end());
                            continue;
                        }
                        m = nullPattern.matcher(linea);
                        if (m.find()) {
                            resultado.append("PR_NULL ");
                            linea = linea.substring(m.end());
                            continue;
                        }

                        // Si no se reconoció nada
                        int espacio = linea.indexOf(" ");
                        String error = (espacio == -1) ? linea : linea.substring(0, espacio);
                        System.err.println("Error léxico en línea " + numLinea + ": '" + error + "'");
                        break;
                    }
                }

                bw.write(resultado.toString().strip());
                bw.newLine();
                numLinea++;
            }

            System.out.println("Análisis completado. Ver 'output.txt'.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
