import {Component, OnDestroy, OnInit} from '@angular/core';

import * as EventBus from 'vertx3-eventbus-client';
import {Alarm} from './model/alarm';
import {last} from 'rxjs/operators';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'alarms statistics';

  eventBusAddress = 'alarms-sock';

  private eventBus;
  private alarmReceiverAddress = 'http://localhost:8888';

  chartOptions = {
    responsive: true
  };

  chartData = [
    { data: [0], label: 'Alarms' }
  ];

  chartLabels = [this.datePipe.transform(new Date(), 'yyyy/MM/dd HH:ss:SSS')];

  constructor(private datePipe: DatePipe) { }

  ngOnInit(): void {
    this.eventBus = new EventBus(this.alarmReceiverAddress + '/eventbus');
    this.eventBus.onopen = () => {
      const self = this;
      self.eventBus.registerHandler(this.eventBusAddress, (error, message) => {
        const alarm: Alarm = JSON.parse(JSON.stringify(message.body));
        const lastData = this.chartData[this.chartData.length - 1];

        this.chartLabels.push(alarm.time);
        const lastCount = lastData.data[lastData.data.length - 1];
        lastData.data.push(lastCount + 1);
      });
    };
    this.eventBus.enableReconnect(true);
  }

  ngOnDestroy(): void {
    this.eventBus.close();
  }
}
