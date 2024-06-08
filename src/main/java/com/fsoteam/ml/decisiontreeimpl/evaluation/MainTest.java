package com.fsoteam.ml.decisiontreeimpl.evaluation;

import com.fsoteam.ml.decisiontreeimpl.decisionTree.*;
import com.fsoteam.ml.decisiontreeimpl.model.*;
import com.fsoteam.ml.decisiontreeimpl.utils.CustomFileReader;

import java.io.*;
import java.util.*;




public class MainTest {
    protected static List<DecisionTreeClass> classes=new ArrayList<>();

    public static void main(String[] args) throws IOException {

        //String fileName = "weather.nominal.arff";
        String fileName = "weather.nominal.arff";

        Scanner clavier = new Scanner(System.in);
        List<Instance> datasets = new ArrayList<>();
        List<Attribute> attributes = new ArrayList<>();
        CustomFileReader fichier = new CustomFileReader(fileName);
        DecisionTree arbre ;
        Node racine = new Node();
        datasets = fichier.getDataSet();
        attributes = fichier.getAttributs();


        int i=1;
        for(Branch b:attributes.get(attributes.size() - 1).getBranches()){
            classes.add( new DecisionTreeClass(i,b.getValue(),0));
            i++;
        }
        int nombreClasse =classes.size();


        attributes.remove(attributes.size() - 1);
        System.out.println(datasets.size() + "\n");
        System.out.println(attributes.size() + "\n");
        System.out.println(classes.size() + "\n");
        System.out.println("\nsize of dataSet : " + datasets.size() + "\nsize of attributes : " + attributes.size() + "\n");
        DataSet corpus = new DataSet(datasets);

        arbre = new Id3DecisionTreeImpl(racine, classes, attributes);

        while (true) {
            System.out.println("Saisir votre choix");
            System.out.println("1 -Pour Utiliser le datasets d'entraînement pour le test");
            System.out.println("2 -Pour Utiliser diviser une partie pour l'entraînement et une partie pour le test");
            System.out.println("3 -Pour Utiliser le Validation croisée  pour le test");
            System.out.println("4 -Pour Entrer les valeurs des attributes d'une instance est connu la prédiction");
            int choix = clavier.nextInt();
            switch (choix) {
                case 1:
                    System.out.println("vous avez choisir le premier choix ");
                    arbre.train(datasets);
                    ConfusionMatrix M = new ConfusionMatrix(arbre.generateConfusionMatrix(datasets),nombreClasse);
                    for (int[] tabi : M.getMatrix()) {
                        for (int index : tabi) {
                            System.out.print(index + " ");
                        }
                        System.out.println();
                    }
                    arbre.displayTreeString( arbre.getRoot(), "   ");
                    break;
                case 2:
                    System.out.println("vous avez choisir le deuxième choix ");
                    System.out.println("Entrer le pourcentage du test 'entier entre 1-99' ");
                    int Pourc = clavier.nextInt();
                    double k = (double) Pourc * datasets.size() / 100;
                    System.out.println("k = " + k);
                    TrainTest divise = corpus.trainTest(k);
                    List<Instance> train = divise.getTrain();
                    List<Instance> test = divise.getTest();
                    System.out.println("Size of Train : " + train.size());
                    System.out.println("Size of Test : " + test.size());
                    arbre.train(train);
                    ConfusionMatrix M1 = new ConfusionMatrix(arbre.generateConfusionMatrix(test),nombreClasse);
                    for (int[] tabi : M1.getMatrix()) {
                        for (int index : tabi) {
                            System.out.print(index + " ");
                        }
                        System.out.println();
                    }
                    arbre.displayTreeString( arbre.getRoot(), "   ");
                    break;
                case 3:
                    System.out.println("vous avez choisir le troisième choix ");
                    System.out.println("Vous voulez diviser le corpus d'apprentissage à combien");
                    int Q = clavier.nextInt();
                    List<TrainTest> devises = corpus.crossValidation(Q);
                    int n = 1;
                    List<ConfusionMatrix> MatricesConf = new ArrayList<>();
                    for (TrainTest devise : devises) {
                        System.out.println("corpus " + n + "\n------------------------------------");
                        List<Instance> trainCross = devise.getTrain();
                        List<Instance> testCross = devise.getTest();
                        System.out.println("Size of Train : " + trainCross.size());
                        System.out.println("Size of Test : " + testCross.size());
                        n++;
                        arbre.train(trainCross);
                        ConfusionMatrix M2 = new ConfusionMatrix(arbre.generateConfusionMatrix(testCross),nombreClasse);
                        MatricesConf.add(M2);
                        /*for (int[] tabi : M2.elements) {
                            for (int index : tabi) {
                                System.out.print(index + " ");
                            }
                            System.out.println();
                        }*/
                        arbre.displayTreeString( arbre.getRoot(), "   ");
                    }

                    int[][] matrice = new int[nombreClasse][nombreClasse];
                    for (ConfusionMatrix Matrice : MatricesConf) {
                        for (int g = 0; g < nombreClasse; g++) {
                            for (int h = 0; h < nombreClasse; h++) {
                                matrice[g][h] += Matrice.getMatrix()[g][h];
                            }
                        }
                    }

                    ConfusionMatrix MatriceFinal = new ConfusionMatrix(matrice,nombreClasse);
                    for (int[] tabi : MatriceFinal.getMatrix()) {
                        for (int indexo : tabi) {
                            System.out.print(indexo + " ");
                        }
                        System.out.println();
                    }
                    break;
                case 4:
                    System.out.println("vous avez choisir le quatrième choix ");
                    System.out.println("Exemple d'ensemble des données pour weather nominal:sunny,cool,normal,FALSE");
                    System.out.println("Exemple d'ensemble des données pour contact-lenses:'young,myope,yes,normal'classe=>hard");

                    String Ensoleille, Temperature, Humidite, Vent;
                    arbre.train(datasets);
                    System.out.println("----->1:saisir le premier attribut "+attributes.get(0).getAttributeName());
                    clavier.nextLine();
                    Ensoleille = clavier.nextLine();
                    System.out.println("----->2:saisir la deuxième attribut "+attributes.get(1).getAttributeName());
                    Temperature = clavier.nextLine();
                    System.out.println("----->3:saisir la troisième attribut "+attributes.get(2).getAttributeName());
                    Humidite = clavier.nextLine();
                    System.out.println("----->4:saisir la quatrième attribut "+attributes.get(3).getAttributeName());
                    Vent = clavier.nextLine();
                    List<String> attList = new ArrayList<>();
                    attList.add(Ensoleille);
                    attList.add(Temperature);
                    attList.add(Humidite);
                    attList.add(Vent);
                    Instance instance = new Instance(99, attList);
                    System.out.println(arbre.evaluate(instance));
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
}