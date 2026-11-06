package service;

public class BackupProcessDeployer implements Runnable{

	private final  BackupManager backupManager;
	private final String uid;
	
	
	//	CONSTRUCTOR DEL BACKUP DE WORKOUTS
	public BackupProcessDeployer(BackupManager backupManager) {
		this.backupManager = backupManager;
		this.uid = null;
		
	}
	
	public BackupProcessDeployer(BackupManager backupManager, String uid) {
		this.backupManager= backupManager;
		this.uid= uid;
	}
	
	@Override
	public void run() {
		
		try {
			if(uid == null) {
				backupManager.realizarBackupWorkouts();
				
				System.out.println("Backup de workouts completado correctamente");
			
			}else {
				backupManager.realizarBackupUsuario(uid);
				System.out.println("Backup de usuario completado correctamente");
			}
		} catch(InterruptedException exc){
			Thread.currentThread().interrupt();
			System.err.println("El proceso de backup ha sido interrumpido");
		
		} catch(Exception exception) {
			System.err.println("Ha ocurrido un error durante el proceso de backup: "+exception.getMessage());
		} 
	}

}
