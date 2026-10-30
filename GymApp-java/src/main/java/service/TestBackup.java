package service;

import java.nio.file.Path;
import java.util.Map;

import dao.ExerciseDAO;
import dao.SerieDAO;
import dao.WorkoutDAO;
import model.Workout;

public class TestBackup {

    public static void main(String[] args) throws Exception {

    	 // Inicializar los DAO siguiendo sus dependencias.
        SerieDAO serieDAO = new SerieDAO();

        ExerciseDAO exerciseDAO = new ExerciseDAO(serieDAO);

        WorkoutDAO workoutDAO = new WorkoutDAO(exerciseDAO);

        // Ejecutar la prueba.
        ejecutarPrueba(workoutDAO);
    }

    private static void ejecutarPrueba(WorkoutDAO workoutDAO)
            throws Exception {

        // PASO 2: Crear el servicio.
        BackupDataService backupDataService =
                new BackupDataService(workoutDAO);

        // PASO 3: Obtener los workouts completos.
        Map<String, Workout> workouts =
                backupDataService.obtenerWorkoutsBackup();

        System.out.println(
                "Workouts recuperados: " + workouts.size()
        );

        // PASO 4: Crear el escritor.
        BackupWritter writer =
                new BackupWritter(Path.of("backups"));

        // PASO 5: Escribir el backup.
        writer.escribirWorkouts(workouts);

        System.out.println(
                "Backup generado correctamente."
        );
    }
}