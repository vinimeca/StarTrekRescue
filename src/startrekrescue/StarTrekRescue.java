package startrekrescue;

import java.util.Random;
import javax.swing.JOptionPane;
import redirect.Redirect;

/**
 *
 * @author Vinicius Meca
 */
public class StarTrekRescue {

    public static int linhas = 10; //Define o numero de linhas
    public static int colunas = 10; //Define o numero de colunas
    public static int num_tripulantes = 3; //Define o numero de tripulantes perdidos
    public static int[] acertou = new int[num_tripulantes]; //Vetor que grava o que ja acertou
    public static int[] adjacente = new int[2]; //Vetor que salva linha adjacente;

    public static int[][] planeta = new int[colunas][linhas]; //Matriz planeta
    public static int[][] tripulantes = new int[num_tripulantes][2]; //Matriz tripulantes
    public static int[] sinalizador = new int[2]; //Vetor sinalizador
    public static int tentativas = 0; //Variavel que grava numero de tentativas
    public static int acertos = 0; //Variavel que grava numero de acertos 

    public static void main(String[] args) {
                
        new Redirect(); //redireciona para classe Redirect que mostra saída em um JFrame 
        
    }
    

    //MEtodo Realiza busca enquanto nao encontrar todos os tripulantes perdidos
    public static void searchTripulantes() {
        do {
            showPlaneta(planeta);
            if (launchSinalizador(sinalizador)) {
                tentativas++;
            } else {
                searchTripulantes();
            }

            if (acertou(sinalizador, tripulantes)) {
                acertos++;
            }

            alterPlaneta(sinalizador, tripulantes, planeta);

        } while (acertos != num_tripulantes);
    }
    //Fim metodo realiza busca enquanto nao encontrar todos os tripulantes perdidos

    //Metodo para iniciar planeta
    public static void initPlaneta(int[][] planeta) {
        for (int linha = 0; linha < colunas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                planeta[linha][coluna] = -1;
            }
        }
    }
    //Fim metodo para iniciar planeta

    //Metodo para mostrar planeta
    public static void showPlaneta(int[][] planeta) {

        String colunas_planeta = "";

        //Monta as colunas
        for (int i = 1; i <= colunas; i++) {
            colunas_planeta = colunas_planeta + "\t" + i;
        }
        
        System.out.println(colunas_planeta);
        //Fim Monta as colunas

        System.out.println();

        //Monta as linhas e os sinalizadores
        for (int linha = 0; linha < linhas; linha++) {
            System.out.print((linha + 1) + "");
            for (int coluna = 0; coluna < colunas; coluna++) {
                if (planeta[linha][coluna] == -1) {
                    System.out.print("\t" + "");
                } else if (planeta[linha][coluna] == 0) {
                    System.out.print("\t" + "*"); //Mostra * quando nao encontra nada
                } else if (planeta[linha][coluna] == 1) {
                    System.out.print("\t" + "X"); //Mostra X quando encontra um tripulante
                } else if (planeta[linha][coluna] == 2) {
                    System.out.print("\t" + "!"); //Mostra ! quando encontra tripulante nas adjacentes
                }

            }
            System.out.println();
            System.out.println();
        }
        //Fim Monta as linhas e os sinalizadores

    }
    //Fim Metodo para mostrar planeta

    //Metodo para iniciar tripulantes
    public static void initTripulantes(int[][] tripulantes) {
        Random sorteio = new Random();

        //Sorteia as posices dos tripulantes
        for (int i = 0; i < num_tripulantes; i++) {
            tripulantes[i][0] = sorteio.nextInt(linhas);
            tripulantes[i][1] = sorteio.nextInt(colunas);

            //tripulantes[i][0] = 4;
            //tripulantes[i][1] = 0;
            acertou[i] = 0;

            //Não deixa sortear um tripulantes na mesma posíção duas vezes
            for (int antes = 0; antes < i; antes++) {
                if ((tripulantes[i][0] == tripulantes[antes][0]) && (tripulantes[i][1] == tripulantes[antes][1])) {
                    do {
                        tripulantes[i][0] = sorteio.nextInt(linhas);
                        tripulantes[i][1] = sorteio.nextInt(colunas);
                    } while ((tripulantes[i][0] == tripulantes[antes][0]) && (tripulantes[i][1] == tripulantes[antes][1]));
                }
            }
            //Fim Não deixa sortear um tripulantes na mesma posíção duas vezes
            //System.out.println("tripulante " + i + " está em (" + tripulantes[i][0] + "," + tripulantes[i][1] + ")");
        }
        //Fim Sorteia as posices dos tripulantes
    }
    //Fim Metodo para iniciar tripulantes

    //Metodo para lancar sinalizador
    public static boolean launchSinalizador(int[] sinalizador) {
        //System.out.println("Digite as coordenadas que deseja disparar o sinalizador...");
        String msgLinha = JOptionPane.showInputDialog("Digite a Linha da coordenada que deseja enviar o sinalizador!");

        if (msgLinha.equals("")) {
            JOptionPane.showMessageDialog(null, "Você não digitou a linha, tente novamente!");
            return false;
        }

        if (!isNumber(msgLinha)) {
            JOptionPane.showMessageDialog(null, "Você não digitou um número, tente novamente!");
            return false;
        }

        int linha = Integer.parseInt(msgLinha);

        //Verifica se lancou sinalizador dentro do planeta
        if (linha <= 0 || linha > linhas) {
            JOptionPane.showMessageDialog(null, "Você está disparando o sinalizador fora do planeta, tente novamente!");
            return false;
        } else {
            sinalizador[0] = linha;
            sinalizador[0]--;
        }

        String msgColuna = JOptionPane.showInputDialog("Digite a Coluna da coordenada que deseja enviar o sinalizador!");

        if (msgColuna.equals("")) {
            JOptionPane.showMessageDialog(null, "Você não digitou a coluna, tente novamente!");
            return false;
        }

        if (!isNumber(msgColuna)) {
            JOptionPane.showMessageDialog(null, "Você não digitou um número, tente novamente!");
            return false;
        }

        int coluna = Integer.parseInt(msgColuna);

        if (coluna <= 0 || coluna > colunas) {
            JOptionPane.showMessageDialog(null, "Você está disparando o sinalizador fora do planeta, tente novamente!");
            return false;
        } else {
            sinalizador[1] = coluna;
            sinalizador[1]--;
        }
        //Fim Verifica se lancou sinalizador dentro do planeta

        System.out.println();
        return true;
    }
    //Fim Metodo para lancar sinalizador

    //Retorna true se a String passada contiver apenas caracteres numericos     
    private static boolean isNumber(String campo) {
        return campo.matches("[0-9]+");
    }
    //Fim Retorna true se a String passada contiver apenas caracteres numericos     

    //Metodo que verifica se encontrou um tripulante e nao permite que conte duas vezes o mesmo
    public static boolean acertou(int[] sinalizador, int[][] tripulantes) {

        for (int i = 0; i < num_tripulantes; i++) {
            if (sinalizador[0] == tripulantes[i][0] && sinalizador[1] == tripulantes[i][1] && acertou[i] == 0) {
                acertou[i] = 1;
                return true;
            }
        }
        return false;
    }
    //Fim Metodo que verifica se encontrou um tripulante e nao permite que conte duas vezes o mesmo

    //Metodo que verifica se encontrou um tripulante
    public static boolean acertou2(int[] sinalizador, int[][] tripulantes) {

        for (int i = 0; i < num_tripulantes; i++) {
            if (sinalizador[0] == tripulantes[i][0] && sinalizador[1] == tripulantes[i][1]) {
                return true;
            }
        }
        return false;
    }
    //Fim Metodo que verifica se encontrou um tripulante

    //Metodo que da update no planeta apos tentativas
    public static void alterPlaneta(int[] sinalizador, int[][] tripulantes, int[][] planeta) {
        if (acertou2(sinalizador, tripulantes)) {
            planeta[sinalizador[0]][sinalizador[1]] = 1;
        } else if (verificaAdjacentes(sinalizador, tripulantes, planeta)) {
            planeta[sinalizador[0]][sinalizador[1]] = 2;
            setAdjascentes(sinalizador, planeta, 2);
        } else {
            planeta[sinalizador[0]][sinalizador[1]] = 0;
            setAdjascentes(sinalizador, planeta, 0);
        }
    }
    //Fim Metodo que da update no planeta apos tentativas

    //Metodo que verifica se ha tripulante nas adjacentes
    public static boolean verificaAdjacentes(int[] sinalizador, int[][] tripulantes, int[][] planeta) {

        int linha = sinalizador[0];
        int coluna = sinalizador[1];

        for (int i = 0; i < num_tripulantes; i++) {

            int user_linha = tripulantes[i][0];
            int user_coluna = tripulantes[i][1];

            if ((linha + 1 == user_linha && coluna + 1 == user_coluna) && planeta[linha + 1][coluna + 1] != 1
                    || (linha - 1 == user_linha && coluna - 1 == user_coluna) && planeta[linha - 1][coluna - 1] != 1
                    || (linha + 1 == user_linha && coluna - 1 == user_coluna) && planeta[linha + 1][coluna - 1] != 1
                    || (linha - 1 == user_linha && coluna + 1 == user_coluna) && planeta[linha - 1][coluna + 1] != 1
                    || (linha + 1 == user_linha && coluna == user_coluna) && planeta[linha + 1][coluna] != 1
                    || (linha == user_linha && coluna + 1 == user_coluna) && planeta[linha][coluna + 1] != 1
                    || (linha - 1 == user_linha && coluna == user_coluna) && planeta[linha - 1][coluna] != 1
                    || (linha == user_linha && coluna - 1 == user_coluna) && planeta[linha][coluna - 1] != 1) {
                return true;
            }
        }
        return false;
    }
     //Fim Metodo que verifica se ha tripulante nas adjacentes    

    //Metodo que seta as adjacentes
    public static void setAdjascentes(int[] sinalizador, int[][] planeta, int type) {

        int linha = sinalizador[0];
        int coluna = sinalizador[1];

        if (linha > 0 && linha < linhas - 1
                && coluna > 0 && coluna < colunas - 1) {

            if (planeta[linha + 1][coluna + 1] != 1) {
                planeta[linha + 1][coluna + 1] = type;
            }
            if (planeta[linha - 1][coluna - 1] != 1) {
                planeta[linha - 1][coluna - 1] = type;
            }
            if (planeta[linha + 1][coluna] != 1) {
                planeta[linha + 1][coluna] = type;
            }
            if (planeta[linha][coluna + 1] != 1) {
                planeta[linha][coluna + 1] = type;
            }
            if (planeta[linha - 1][coluna] != 1) {
                planeta[linha - 1][coluna] = type;
            }
            if (planeta[linha][coluna - 1] != 1) {
                planeta[linha][coluna - 1] = type;
            }
            if (planeta[linha + 1][coluna - 1] != 1) {
                planeta[linha + 1][coluna - 1] = type;
            }
            if (planeta[linha - 1][coluna + 1] != 1) {
                planeta[linha - 1][coluna + 1] = type;
            }

        } else if (linha == 0
                && coluna > 0 && coluna < colunas - 1) {

            if (planeta[linha + 1][coluna] != 1) {
                planeta[linha + 1][coluna] = type;
            }
            if (planeta[linha + 1][coluna + 1] != 1) {
                planeta[linha + 1][coluna + 1] = type;
            }
            if (planeta[linha][coluna + 1] != 1) {
                planeta[linha][coluna + 1] = type;
            }
            if (planeta[linha + 1][coluna - 1] != 1) {
                planeta[linha + 1][coluna - 1] = type;
            }
            if (planeta[linha][coluna - 1] != 1) {
                planeta[linha][coluna - 1] = type;
            }

        } else if (coluna == 0
                && linha > 0 && linha < linhas - 1) {

            if (planeta[linha - 1][coluna] != 1) {
                planeta[linha - 1][coluna] = type;
            }
            if (planeta[linha - 1][coluna + 1] != 1) {
                planeta[linha - 1][coluna + 1] = type;
            }
            if (planeta[linha][coluna + 1] != 1) {
                planeta[linha][coluna + 1] = type;
            }
            if (planeta[linha + 1][coluna + 1] != 1) {
                planeta[linha + 1][coluna + 1] = type;
            }
            if (planeta[linha + 1][coluna] != 1) {
                planeta[linha + 1][coluna] = type;
            }

        } else if (linha == linhas - 1
                && coluna > 0 && coluna < colunas - 1) {

            if (planeta[linha][coluna - 1] != 1) {
                planeta[linha][coluna - 1] = type;
            }
            if (planeta[linha - 1][coluna - 1] != 1) {
                planeta[linha - 1][coluna - 1] = type;
            }
            if (planeta[linha - 1][coluna] != 1) {
                planeta[linha - 1][coluna] = type;
            }
            if (planeta[linha - 1][coluna + 1] != 1) {
                planeta[linha - 1][coluna + 1] = type;
            }
            if (planeta[linha][coluna + 1] != 1) {
                planeta[linha][coluna + 1] = type;
            }

        } else if (coluna == colunas - 1
                && linha > 0 && linha < linhas - 1) {

            if (planeta[linha - 1][coluna] != 1) {
                planeta[linha - 1][coluna] = type;
            }
            if (planeta[linha - 1][coluna - 1] != 1) {
                planeta[linha - 1][coluna - 1] = type;
            }
            if (planeta[linha][coluna - 1] != 1) {
                planeta[linha][coluna - 1] = type;
            }
            if (planeta[linha + 1][coluna - 1] != 1) {
                planeta[linha + 1][coluna - 1] = type;
            }
            if (planeta[linha + 1][coluna] != 1) {
                planeta[linha + 1][coluna] = type;
            }

        } else if (linha == 0 && coluna == 0) {

            if (planeta[linha][coluna + 1] != 1) {
                planeta[linha][coluna + 1] = type;
            }
            if (planeta[linha + 1][coluna + 1] != 1) {
                planeta[linha + 1][coluna + 1] = type;
            }
            if (planeta[linha + 1][coluna] != 1) {
                planeta[linha + 1][coluna] = type;
            }

        } else if (linha == linhas - 1 && coluna == 0) {

            if (planeta[linha - 1][coluna] != 1) {
                planeta[linha - 1][coluna] = type;
            }
            if (planeta[linha - 1][coluna + 1] != 1) {
                planeta[linha - 1][coluna + 1] = type;
            }
            if (planeta[linha][coluna + 1] != 1) {
                planeta[linha][coluna + 1] = type;
            }

        } else if (linha == linhas - 1 && coluna == colunas - 1) {

            if (planeta[linha - 1][coluna] != 1) {
                planeta[linha - 1][coluna] = type;
            }
            if (planeta[linha - 1][coluna - 1] != 1) {
                planeta[linha - 1][coluna - 1] = type;
            }
            if (planeta[linha][coluna - 1] != 1) {
                planeta[linha][coluna - 1] = type;
            }

        } else if (linha == 0 && coluna == colunas - 1) {

            if (planeta[linha][coluna - 1] != 1) {
                planeta[linha][coluna - 1] = type;
            }
            if (planeta[linha + 1][coluna - 1] != 1) {
                planeta[linha + 1][coluna - 1] = type;
            }
            if (planeta[linha + 1][coluna] != 1) {
                planeta[linha + 1][coluna] = type;
            }

        }
    }
    //Fim Metodo que seta as adjacentes

}
