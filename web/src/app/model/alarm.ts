export class Alarm {
  constructor(public producerId: string,
              public resourceId: string,
              public severity: string,
              public time: string) { }
}
