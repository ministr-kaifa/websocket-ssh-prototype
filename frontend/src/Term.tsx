import { FC, useEffect, useRef } from 'react';
import { Terminal } from '@xterm/xterm';
import { FitAddon } from '@xterm/addon-fit';
import '@xterm/xterm/css/xterm.css';
import { AttachAddon } from '@xterm/addon-attach';

const theme = {
  foreground: '#ebeef5',
  background: '#1d2935',
  cursor: '#e6a23c',
  black: '#000000',
  brightBlack: '#555555',
  red: '#ef4f4f',
  brightRed: '#ef4f4f',
  green: '#67c23a',
  brightGreen: '#67c23a',
  yellow: '#e6a23c',
  brightYellow: '#e6a23c',
  blue: '#409eff',
  brightBlue: '#409eff',
  magenta: '#ef4f4f',
  brightMagenta: '#ef4f4f',
  cyan: '#17c0ae',
  brightCyan: '#17c0ae',
  white: '#bbbbbb',
  brightWhite: '#ffffff',
};

const Term: FC = () => {
  const terminalRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const terminal = new Terminal({
      theme,
      allowProposedApi: true,
    });
    const socket = new WebSocket('ws://localhost:8080/api/stands/1/shells/1');

    const fitAddon = new FitAddon();
    const attachAddon = new AttachAddon(socket);

    terminal.loadAddon(fitAddon);
    terminal.loadAddon(attachAddon);

    if (terminalRef.current) {
      (async () => {
        await terminal.open(terminalRef.current);
        await fitAddon.fit();
      })();
    }

    return () => {
      terminal.dispose();
      socket.close();
    };
  }, []);

  return (
    <div style={{ height: '250px', width: '600px', margin: '50px' }}>
      <div style={{ width: '100%', height: '100%' }} ref={terminalRef} />
    </div>
  );
};

export default Term;
