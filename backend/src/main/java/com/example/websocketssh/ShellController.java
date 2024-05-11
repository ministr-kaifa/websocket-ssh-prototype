package com.example.websocketssh;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShellController {

  private final ShellService service;

  public ShellController(ShellService service) {
    this.service = service;
  }
  
  @PatchMapping("/api/stands/{standId}/shells/{shellId}/window")
  public void resize(@RequestBody ResizeRequest request, @PathVariable long standId, @PathVariable long shellId) {
    service.resizeShellWindow(standId, shellId, request.rows(), request.columns());
  }
}
