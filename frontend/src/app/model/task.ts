export interface Task {
    id: number;
    title: string;
    description: string;
    dueDate: string; // ISO string format
    completed: boolean;
}