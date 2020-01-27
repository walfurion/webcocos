/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

/**
 *
 * @author Allan G.
 */
public class test {

    public static void main(String[] args) {
        String algo = "";
        System.out.println("NEW PASS: " + PasswordGenerator.getPassword(8));

        String msg = "<html>\n"
                + "<head> </head>\n"
                + "<body>\n"
                + "<table width=100%>\n"
                + "<tr align=center><td><img src=\"http://www.uno-plus.com/unopetrol/img/uno.png\"></td></tr>\n"
                + "</table>\n"
                + "<br>\n"
                + "<br>\n"
                + "<table width=100% cellpadding=0 cellspacing=0>\n"
                + " <tr>\n"
                + "  <td>\n"
                + "  <p style=\"text-align:center;text-align:center\"><span style=\"font-size:12.0pt;line-height:119%;font-family:Calibri;\n"
                + "  color:#0000CC;font-weight:bold\"><h1 align=\"center\"><titulo></h1></span></p>\n"
                + "  <br>\n"
                + "  <p style=\"text-align:center;text-align:center\"><span style=\"font-size:12.0pt;line-height:119%;font-family:Calibri;\n"
                + "  color:##000000;font-weight:bold\"><h2 align=\"center\"><cliente></h2></span></p>\n"
                + "  <br>\n"
                + "  <p style=\"text-align:center;text-align:center\"><span style=\"font-size:12.0pt;line-height:119%;font-family:Calibri\">\n"
                + "  <cabecera01></span></p>\n"
                + "  <p style=\"text-align:center;text-align:center\"><span style=\"font-size:12.0pt;line-height:119%;font-family:Calibri\">\n"
                + "  <cabecera02> </span></p>\n"
                + "  <p style=\"text-align:center;text-align:center\"><span style=\"font-size:12.0pt;line-height:119%;font-family:Calibri;\n"
                + "  text-decoration:underline\"><cabecera03></span></p>\n"
                + " </td>\n"
                + " </tr>\n"
                + "</table>\n"
                + "</br></br>\n"
                + "</br>\n"
                + "<table border=\"1\" style=\"width:100%\">\n"
                + "  <tr name=\"tblData\" style=\"background-color:white; font-size: 20px; font-family:Calibri; color: #000000;font-weight:bold; text-align: left;\">\n"
                + "    <th>XXXXX</th>\n"
                + "    <th>YYYYY</th>\n"
                + "  </tr>\n"
                + "<cuerpo>    \n"
                + "</table>\n"
                + " </br></br>\n"
                + " </br></br>\n"
                + " <table cellpadding=0 cellspacing=0 width=28% align=center> \n"
                + "  \n"
                + " </table>\n"
                + " </br></br>\n"
                + " </br></br>\n"
                + "	<p style=\"text-align:center;text-align:center\">\n"
                + "		<span style=\"font-size:10.0pt;line-height:119%;font-family:Calibri;color:#9C9C9C;font-weight:bold\">\n"
                + "			Este correo electr&oacute;nico, su contenido y anexos son CONFIDENCIALES y pueden contener informaci&oacute;n PRIVILEGIADA para uso exclusivo de su destinatario. Si ha recibido este correo por error, o si no es su destinatario, por favor no lo copie o distribuya, ni realice ninguna acci&oacute;n relacionada con el mismo. En su lugar, por favor notif&iacute;quelo al remitente y b&oacute;rrelo de su sistema. Las opiniones expresadas en este correo son las de su autor y no son necesariamente compartidas o apoyadas por la compa&ntilde;&iacute;a. UNO PETROL no asume a trav&eacute;s de este correo obligaciones ni se responsabiliza del contenido del mismo. \n"
                + "		</span>\n"
                + "	</p>\n"
                + "</body>\n"
                + "</html>";
        
        
        String var2 = msg.replaceAll("XXXXX", "ALLAN");
        String varf = var2.replaceAll("YYYYY", "ALLAN");
        System.out.println(" NUEVO "+varf);

    }

}
