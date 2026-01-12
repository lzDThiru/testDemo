import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/service/task.service';
import { Task } from 'src/app/model/task';


@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],

})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  constructor(readonly taskService: TaskService, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.taskService.getTasks().subscribe((data) => {
      this.tasks = data;
      this.changeDetectorRef.detectChanges();
    });

  }

  editTask(task: Task): void {
    // Implement edit task functionality
    console.log('Editing task:', task);
  }

  deleteTask(taskId: number): void {
    this.taskService.deleteTask(taskId).subscribe(() => {
      this.tasks = this.tasks.filter(task => task.id !== taskId);
      this.changeDetectorRef.detectChanges();
    });
  }
}
