/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Zero
 */
public class AI2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GA g = new GA(100);
        g.run(40, 20, 20);

    }

}
class GA {

    int[][] population;
    int[] idealsample;
    int poulationsize;

    GA(int size) {
        BufferedImage img = null;
        File file = new File("imageB.bmp");
        try {
            img = ImageIO.read(file);

            BufferedImage result = new BufferedImage(
                    img.getWidth(),
                    img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(img, 0, 0, Color.WHITE, null);
            graphic.dispose();
            int height = result.getHeight();
            int width = result.getWidth();
            idealsample = new int[height * width];
            for (int i = 0; i < height * width; i++) {

                int rgb = result.getRGB(i % width, i / width);
                if (rgb == -1) {
                    idealsample[i] = 0;
                } else {

                    idealsample[i] = 1;
                }
            }
            File output = new File("input.bmp");
            ImageIO.write(result, "bmp", output);

            Random rand = new Random();
            population = new int[size][height * width];
            poulationsize = size;
            for (int k = 0; k < poulationsize; k++) {
                int[] sample = new int[height * width];
                for (int i = 0; i < height * width; i++) {
                    sample[i] = rand.nextInt(2);

                }
                population[k] = sample;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run(int s, int c, int m) {
        int maxfitness = population[0].length;
        int noofgeneartions = 1;
        int[] FitnessArray = new int[poulationsize];
        int[][] Newpopulation;
        int selectionrate = s;
        int crossoverrate = c;
        int mutationrate = m;
        int newpopulationsize;
        System.out.println("Max Possible Fitness: " +maxfitness);
        BufferedImage img = null;
        File file = new File("imageB.bmp");
        int bestsample[] = new int[population[0].length];
        
        int solutionfoundonGeneration=0;
        int FitnessvalueofBestSolution=0;
        
        FitnessSort(population);
        System.out.println("Max Fitness: " + FitnessFuntion(population[0]));
        System.out.println("Generation: " + 0);
        System.out.println();
        
        List<int[]> bestpopulation;
        while (noofgeneartions <= 100000) {
            
            
     
           Newpopulation = new int[poulationsize][population[0].length];

           bestpopulation= Selectionrouletewheel(population, Newpopulation, selectionrate);
            //Selection(population, Newpopulation, FitnessArray, selectionrate);
       
            
           // newpopulationsize = crossover3withbestpop(Newpopulation, crossoverrate, selectionrate);
             newpopulationsize=crossover3withwholepopulation(Newpopulation, crossoverrate, selectionrate);
            //newpopulationsize=crossover3withnonbestpop(Newpopulation,crossoverrate,selectionrate,bestpopulation);

            //newpopulationsize = Mutationwithbestpop(Newpopulation, mutationrate, newpopulationsize, 2);
             newpopulationsize=Mutationwithwholepopulation(Newpopulation, mutationrate, newpopulationsize,2);
            //newpopulationsize=Mutationwithnonbestpopulation(Newpopulation, mutationrate, newpopulationsize,2,bestpopulation);

            population = Newpopulation;
            FitnessSort(population);
            if (FitnessvalueofBestSolution < FitnessFuntion(population[0])) {   
                FitnessvalueofBestSolution=FitnessFuntion(population[0]);;
                solutionfoundonGeneration=noofgeneartions;
                for (int i = 0; i < bestsample.length; i++) {
                    bestsample[i] = population[0][i];
                }
            }
            System.out.println("Max Fitness: " + FitnessFuntion(population[0]));
            System.out.println("Generation: " + noofgeneartions);
            System.out.println();
            noofgeneartions++;
        }
        
        System.out.println("Fitness value of Best Solution: " + FitnessvalueofBestSolution);
        System.out.println("Solution Found on Generation: " + solutionfoundonGeneration);
        try {
            img = ImageIO.read(file);

            BufferedImage result = new BufferedImage(
                    img.getWidth(),
                    img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(img, 0, 0, Color.WHITE, null);
            graphic.dispose();
            int height = result.getHeight();
            int width = result.getWidth();
            for (int i = 0; i < height * width; i++) {

                if (bestsample[i] == 0) {
                    result.setRGB(i % width, i / width, -1);

                } else {
                    result.setRGB(i % width, i / width, -16777216);
                }
            }
            File output = new File("BestSolutionReachedAfterMaxIteration.bmp");
            ImageIO.write(result, "bmp", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            img = ImageIO.read(file);
            BufferedImage result = new BufferedImage(
                    img.getWidth(),
                    img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(img, 0, 0, Color.WHITE, null);
            graphic.dispose();
            int height = result.getHeight();
            int width = result.getWidth();
            for (int i = 0; i < height * width; i++) {

                if (population[0][i] == 0) {
                    result.setRGB(i % width, i / width, -1);

                } else {
                    result.setRGB(i % width, i / width, -16777216);
                }
            }
            File output = new File("BestSolutionAtMaxIteration.bmp");
            ImageIO.write(result, "bmp", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int FitnessFuntion(int[] sample) {
        int count = 0;
        for (int i = 0; i < sample.length; i++) {
            if (idealsample[i] == sample[i]) {
                count++;
            }
        }
        return count;
    }

    void FitnessSort(int[][] population) {

        MergeSort(population, 0, population.length - 1);
    }

    void MergeSort(int arr[][], int start, int end) {
        if (start < end) {
            int middle = (start + end) / 2;
            MergeSort(arr, start, middle);
            MergeSort(arr, middle + 1, end);
            Merge(arr, start, middle, end);
        }
    }

    void Merge(int arr[][], int start, int middle, int end) {
        int start1 = start;      //Starting index of left sub-array
        int start2 = middle + 1;          //Starting of right sub-array
        int k = 0;
        int temp[][] = new int[end - start + 1][arr[0].length];
        for (int i = 0; i < temp.length; i++) {
            if (start1 > middle) {
                temp[k++] = arr[start2++];
            } else if (start2 > end) {
                temp[k++] = arr[start1++];
            } else if (FitnessFuntion(arr[start1]) > FitnessFuntion(arr[start2])) {
                temp[k++] = arr[start1++];
            } else {
                temp[k++] = arr[start2++];
            }
        }
        for (int i = 0; i < temp.length; i++) {
            arr[start++] = temp[i];
        }
    }

    void Selection(int[][] population, int[][] Newpopulation, int[] FitnessArray, int selectionNumber) {

        for (int i = 0; i < selectionNumber; i++) {
            Newpopulation[i] = population[i];
        }
    }

    List<int[]> Selectionrouletewheel(int[][] population, int[][] Newpopulation, int selectionNumber) {

        Random rand = new Random();
        List<int[]> selected = new ArrayList<>();
        int indexofselected;
        for (int i = 0; i < selectionNumber;) {
            int j = rand.nextInt(100);
            if (j < 80) {
                indexofselected = rand.nextInt(selectionNumber);
                if (!selected.contains(population[indexofselected])) {
                    Newpopulation[i] = population[indexofselected];
                    i++;
                    selected.add(population[indexofselected]);
                }
            } else {
                indexofselected = rand.nextInt(population.length - selectionNumber) + selectionNumber;
                if (!selected.contains(population[indexofselected])) {
                    Newpopulation[i] = population[indexofselected];
                    i++;
                    selected.add(population[indexofselected]);

                }
            }
        }
        return selected;
    }

    int crossover3withbestpop(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int image1 = rand.nextInt(currentpopulationsize);
            int image2 = rand.nextInt(currentpopulationsize);
            while (image2 == image1) {
                image2 = rand.nextInt(currentpopulationsize);
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = Newpopulation[image1][i];
                temp2[i] = Newpopulation[image2][i];
            }
            int crossoverpoint = rand.nextInt(temp1.length - 1)+1;
            int j = rand.nextInt(2);
            if (j == 1) {

                for (int i = 0; i < crossoverpoint; i++) {

                    if (i % 2 == 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            } else {
                for (int i = 0; i < crossoverpoint; i++) {

                    if (i % 2 != 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;

    }

    int crossover3withnonbestpop(int[][] Newpopulation, int rate, int currentpopulationsize, List<int[]> bestpop) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int image1 = rand.nextInt(population.length);
            while(bestpop.contains(population[image1]))
                image1 = rand.nextInt(population.length);
            int image2 = rand.nextInt(population.length);
            while (image2 == image1 || bestpop.contains(population[image1])) {
                image2 = rand.nextInt(population.length);
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
                temp2[i] = population[image2][i];
            }
            int crossoverpoint = rand.nextInt(temp1.length - 1)+1;
            int j = rand.nextInt(2);
            if (j == 1) {

                for (int i = 0; i < crossoverpoint; i++) {

                    if (i % 2 == 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            } else {
                for (int i = 0; i < crossoverpoint; i++) {

                    if (i % 2 != 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
       
        return currentpopulationsize;
     
    }

    int crossover3withwholepopulation(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int image1 = rand.nextInt(population.length);
            int image2 = rand.nextInt(population.length);
            while (image2 == image1) {
                image2 = rand.nextInt(population.length);
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
                temp2[i] = population[image2][i];
            }
            int crossoverpoint = rand.nextInt(temp1.length - 1)+1;
            int j = rand.nextInt(2);
            if (j == 1) {

                for (int i = 0; i < crossoverpoint; i++) {

                    if (i % 2 == 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            } else {
                for (int i = 0; i < crossoverpoint; i++) {

                    if (i % 2 != 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }

    int Mutationwithbestpop(int[][] Newpopulation, int rate, int currentpopulationsize, int Noofbitstoflip) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int noofbitstoflip = rand.nextInt(Noofbitstoflip) + 1;
            int image1 = rand.nextInt(currentpopulationsize);
            int[] temp1 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = Newpopulation[image1][i];
            }
            List selected = new ArrayList<>();
            for (int i = 0; i < noofbitstoflip;) {

                int bittoflip = rand.nextInt(Newpopulation[0].length);
                if (!selected.contains(bittoflip)) {
                    temp1[bittoflip]++;
                    if (temp1[bittoflip] == 2) {
                        temp1[bittoflip] = 0;
                    }
                    selected.add(bittoflip);
                    i++;
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }

    int Mutationwithwholepopulation(int[][] Newpopulation, int rate, int currentpopulationsize, int Noofbitstoflip) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int noofbitstoflip = rand.nextInt(Noofbitstoflip) + 1;
            int image1 = rand.nextInt(population.length);
            int[] temp1 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
            }
            List selected = new ArrayList<>();
            for (int i = 0; i < noofbitstoflip;) {

                int bittoflip = rand.nextInt(Newpopulation[0].length);
                if (!selected.contains(bittoflip)) {
                    temp1[bittoflip]++;
                    if (temp1[bittoflip] == 2) {
                        temp1[bittoflip] = 0;
                    }
                    selected.add(bittoflip);
                    i++;
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }

    int Mutationwithnonbestpopulation(int[][] Newpopulation, int rate, int currentpopulationsize, int Noofbitstoflip,  List<int[]> bestpop) {
         int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int noofbitstoflip = rand.nextInt(Noofbitstoflip) + 1;
            int image1 = rand.nextInt(population.length);
            while(bestpop.contains(population[image1]))
                image1 = rand.nextInt(population.length);

            int[] temp1 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
            }
            List selected = new ArrayList<>();
            for (int i = 0; i < noofbitstoflip;) {

                int bittoflip = rand.nextInt(Newpopulation[0].length);
                if (!selected.contains(bittoflip)) {
                    temp1[bittoflip]++;
                    if (temp1[bittoflip] == 2) {
                        temp1[bittoflip] = 0;
                    }
                    selected.add(bittoflip);
                    i++;
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }
    /*
     int crossoverwithbestpop(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int corssoverpoint = rand.nextInt(currentpopulationsize-2)+1;
            int image1 = rand.nextInt(currentpopulationsize-2)+1;
            int image2 = rand.nextInt(currentpopulationsize-2)+1;
            while (image2 == image1) {
                image2 = rand.nextInt(currentpopulationsize-2)+1;
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = Newpopulation[image1][i];
                temp2[i] = Newpopulation[image2][i];
            }
            for (int i = 0; i < corssoverpoint; i++) {

                int temp = temp1[i];
                temp1[i] = temp2[i];
                temp2[i] = temp;
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;

    }

    int crossoverwithnonbestpop(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int corssoverpoint = rand.nextInt(population.length - rate + 1) + rate - 1;
            int image1 = rand.nextInt(population.length - rate) + rate;
            int image2 = rand.nextInt(population.length - rate) + rate;
            while (image2 == image1) {
                image2 = rand.nextInt(population.length - rate) + rate;
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
                temp2[i] = population[image2][i];
            }
            for (int i = 0; i < corssoverpoint; i++) {

                int temp = temp1[i];
                temp1[i] = temp2[i];
                temp2[i] = temp;
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;

    }

    int crossoverwithwholepopulation(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int corssoverpoint = rand.nextInt(population.length - 1) + 1;
            int image1 = rand.nextInt(population.length);
            int image2 = rand.nextInt(population.length);
            while (image2 == image1) {
                image2 = rand.nextInt(population.length);
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
                temp2[i] = population[image2][i];
            }
            for (int i = 0; i < corssoverpoint; i++) {

                int temp = temp1[i];
                temp1[i] = temp2[i];
                temp2[i] = temp;
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }

    int crossover2withbestpop(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int image1 = rand.nextInt(currentpopulationsize-2)+1;
            int image2 = rand.nextInt(currentpopulationsize-2)+1;
            while (image2 == image1) {
                image2 = rand.nextInt(currentpopulationsize-2)+1;
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = Newpopulation[image1][i];
                temp2[i] = Newpopulation[image2][i];
            }
            int j = rand.nextInt(2);
            if (j == 1) {
                for (int i = 0; i < temp1.length; i++) {

                    if (i % 2 == 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            } else {
                for (int i = 0; i < temp1.length; i++) {

                    if (i % 2 != 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }

    int crossover2withnonbestpop(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int image1 = rand.nextInt(population.length - currentpopulationsize + 1) + currentpopulationsize - 1;
            int image2 = rand.nextInt(population.length - currentpopulationsize + 1) + currentpopulationsize - 1;
            while (image2 == image1) {
                image2 = rand.nextInt(population.length - currentpopulationsize + 1) + currentpopulationsize - 1;
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
                temp2[i] = population[image2][i];
            }
            int j = rand.nextInt(2);
            if (j == 1) {
                for (int i = 0; i < temp1.length; i++) {

                    if (i % 2 == 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            } else {
                for (int i = 0; i < temp1.length; i++) {

                    if (i % 2 != 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }

    int crossover2withwholepopulation(int[][] Newpopulation, int rate, int currentpopulationsize) {
        int k = 0;
        while (k < rate) {
            Random rand = new Random();
            int image1 = rand.nextInt(population.length - 2) + 1;
            int image2 = rand.nextInt(population.length - 2) + 1;
            while (image2 == image1) {
                image2 = rand.nextInt(population.length - 2) + 1;
            }
            int[] temp1 = new int[Newpopulation[0].length];
            int[] temp2 = new int[Newpopulation[0].length];
            for (int i = 0; i < Newpopulation[0].length; i++) {

                temp1[i] = population[image1][i];
                temp2[i] = population[image2][i];
            }
            int j = rand.nextInt(2);
            if (j == 1) {
                for (int i = 0; i < temp1.length; i++) {

                    if (i % 2 == 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            } else {
                for (int i = 0; i < temp1.length; i++) {

                    if (i % 2 != 0) {
                        int temp = temp1[i];
                        temp1[i] = temp2[i];
                        temp2[i] = temp;
                    }
                }
            }
            Newpopulation[currentpopulationsize] = temp1;
            currentpopulationsize++;
            Newpopulation[currentpopulationsize] = temp2;
            currentpopulationsize++;
            k++;
        }
        return currentpopulationsize;
    }
     */
}
